package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.interfaces.BaseWordService;
import de.frittenburger.interfaces.LanguageProcessingService;
import de.frittenburger.interfaces.LanguageWordAnalyser;
import de.frittenburger.interfaces.LevelService;
import de.frittenburger.interfaces.TranslationService;
import de.frittenburger.model.Annotated;
import de.frittenburger.model.AnnotatedText;
import de.frittenburger.model.Annotation;
import de.frittenburger.model.Token;
import de.frittenburger.model.TokenList;
import de.frittenburger.srt.SrtMergeReader;
import de.frittenburger.srt.SrtMergeReaderWrapper;

public class TranslationServiceImpl implements TranslationService {

	

	
	private final Map<String,LevelService> levelServices = new HashMap<>();
	private final Map<String,LanguageProcessingService> processingServices = new HashMap<>();



	public TranslationServiceImpl() throws IOException {
		
		ClassLoader classLoader = SpanishDingLanguageWordAnalyser.class.getClassLoader();

		//de,es
		{
			File fileVerbs = new File(classLoader.getResource("dict/es-de/verbs.txt").getFile());
			BaseWordService baseWordService = new SpanishVerbService(fileVerbs);
			File fileDict = new File(classLoader.getResource("dict/es-de/dict.txt").getFile());
			LanguageWordAnalyser analyser = new SpanishDingLanguageWordAnalyser(new DingDictionary(fileDict),baseWordService);
			LanguageProcessingService languageProcessingService = new LanguageProcessingServiceImpl(analyser);
			processingServices.put("es-de",languageProcessingService);
		}
		{
			File fileVerbs = new File(classLoader.getResource("dict/de-es/verbs.txt").getFile());
			BaseWordService baseWordService = new SpanishVerbService(fileVerbs);
			File fileDict = new File(classLoader.getResource("dict/de-es/dict.txt").getFile());
			LanguageWordAnalyser analyser = new SpanishDingLanguageWordAnalyser(new DingDictionary(fileDict),baseWordService);
			LanguageProcessingService languageProcessingService = new LanguageProcessingServiceImpl(analyser);
			processingServices.put("de-es",languageProcessingService);
		}
		
		
		//de,en
		{
			File fileVerbs = new File(classLoader.getResource("dict/en-de/verbs.txt").getFile());
			BaseWordService baseWordService = new EnglishVerbService(fileVerbs);
			File fileDict = new File(classLoader.getResource("dict/en-de/dict.txt").getFile());
			LanguageWordAnalyser analyser = new SpanishDingLanguageWordAnalyser(new DingDictionary(fileDict),baseWordService);
			LanguageProcessingService languageProcessingService = new LanguageProcessingServiceImpl(analyser);
			processingServices.put("en-de",languageProcessingService);
		}
		{
			File fileVerbs = new File(classLoader.getResource("dict/de-en/verbs.txt").getFile());
			BaseWordService baseWordService = new GermanVerbService(fileVerbs);
			File fileDict = new File(classLoader.getResource("dict/de-en/dict.txt").getFile());
			LanguageWordAnalyser analyser = new SpanishDingLanguageWordAnalyser(new DingDictionary(fileDict),baseWordService);
			LanguageProcessingService languageProcessingService = new LanguageProcessingServiceImpl(analyser);
			processingServices.put("de-en",languageProcessingService);
		}
	
		
		{
			File fileLevel = new File(classLoader.getResource("dict/es/frequency.txt").getFile());
			LevelService levelService = new LevelServiceImpl(fileLevel);
			levelServices.put("es",levelService);
		}
		{
			File fileLevel = new File(classLoader.getResource("dict/de/frequency.txt").getFile());
			LevelService levelService = new LevelServiceImpl(fileLevel);
			levelServices.put("de",levelService);
		}
		{
			File fileLevel = new File(classLoader.getResource("dict/en/frequency.txt").getFile());
			LevelService levelService = new LevelServiceImpl(fileLevel);
			levelServices.put("en",levelService);
		}
		
		
		
	}
	
	
	
	@Override
	public void translate(File mergeFile, File translationFile) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		
		SrtMergeReader reader = new SrtMergeReader(mergeFile);
		SrtMergeReaderWrapper wrapper = new SrtMergeReaderWrapper(reader);
		
		String lang[] = wrapper.getLanguages().toArray(new String[0]);
		LevelService levelService1 = levelServices.get(lang[0]);
		LevelService levelService2 = levelServices.get(lang[1]);
		LanguageProcessingService processingService1 = processingServices.get(lang[0]+"-"+lang[1]);
		LanguageProcessingService processingService2 = processingServices.get(lang[1]+"-"+lang[0]);

		List<AnnotatedRecord> textes = new ArrayList<AnnotatedRecord>();
		for(int i = 0;i < wrapper.size();i++)
		{
			AnnotatedRecord rec = new AnnotatedRecord();
			
			String text1 = wrapper.getText(i,lang[0]);
			String text2 = wrapper.getText(i,lang[1]);

			if(text1 == null || text2 == null)
			{
				System.err.println("Record "+i+" text1 "+text1+" text2 "+text2);
				continue;
			}
			AnnotatedText t1 = getRecord(levelService1,processingService1,text1);
			rec.put(lang[0],t1);
			System.out.println(mapper.writeValueAsString(t1));

			AnnotatedText t2 = getRecord(levelService2,processingService2,text2);
			rec.put(lang[1],t2);
			System.out.println(mapper.writeValueAsString(t2));
		
			textes.add(rec);
		}
		
		mapper.writerWithDefaultPrettyPrinter().writeValue(translationFile, textes);
		
		
		
	}

	
	private AnnotatedText getRecord(LevelService translationService,
			LanguageProcessingService processingService, String text) {
		Tokenizer tokenizer = new Tokenizer();

		TokenList tokens = tokenizer.tokenize(text);

		List<Annotated> annotated = new ArrayList<Annotated>();
		for(Annotation annotation : processingService.process(tokens))
		{
			int maxLevel = 0;
			List<String> words = new ArrayList<String>();
			for(int ix : annotation.getIndices())
			{
				Token t = tokens.get(ix);
				int level = translationService.testLevel(t.getText());
				if(t.getType() != 0) continue;

				if(level > maxLevel)
				{
					maxLevel = level;
				}
				words.add(t.getText());
			}
			
			if(maxLevel > 3)
			{
				Annotated a = new Annotated();
				a.setInfo(annotation.getInfos());
				a.setWords(words);
				annotated.add(a);
			}
			
		}
		
		AnnotatedText atxt = new AnnotatedText();
		atxt.setText(text);
		atxt.setAnnotated(annotated);
		return atxt;
	}

}
