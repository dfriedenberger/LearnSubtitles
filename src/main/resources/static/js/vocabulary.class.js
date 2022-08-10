
VocabularyManager = function(database,language0,language1,bucketId)
{
    this.dataset = [];
    this.ix = 0;
    this.language0 = language0;
    this.language1 = language1;
    this.bucketId = bucketId;
    this.database = database;

    this.templateTitle = Handlebars.compile($("#quiz-title").html());
    this.templateStageActionLable = Handlebars.compile($("#quiz-lable").html());
    
    this.templateResultCorrect = Handlebars.compile($("#quiz-result-correct").html());
    this.templateResultFalse = Handlebars.compile($("#quiz-result-false").html());

   

    //init
    var data = this.database.read('quiz-'+this.bucketId);
    if(data)
    {
        this.ix = data.ix;
        this.language0 = data.language0;
        this.language1 = data.language1;
    }
   
    this._saveState = function()
    {
        var data = { 
            ix : this.ix,
            language0 : this.language0,
            language1 : this.language1
        };
        this.database.write('quiz-' +this.bucketId,data);

    }
    this.setDataset = function(dataset)
    {
        this.dataset = dataset;
        console.log(dataset);
    }


    this.createQuiz = function()
    {
        var manager = this;

        var data = this.dataset.items[this.language0].items;
        var ix = this.ix;

        console.log(data[ix]);

         var title    = $(this.templateTitle({ text : data[ix].text }));
         $("#quiz").empty();
      
         $('#quiz').show();
         $("#question").html(title);

         var correctIx = Math.floor((Math.random() * 4) +  1);
         var falseIx = 0;

         console.log(" correct = "+correctIx);

         for(var i = 1;i <= 4;i++)
         {
            var answer = data[ix].translation;
            var correct = true;
            if(i != correctIx)
            {
                answer = data[ix].quizCandidates[falseIx];
                correct = false;
                falseIx++;
            }

            var lable    = $(this.templateStageActionLable({index : i, answer: answer , correct : correct }));
            lable.on('click',function(ev)
            {
                ev.preventDefault();

                var selectedIndex = $(this).data("index");
                var selectedCorrect = $(this).data("correct");
                console.log("Select "+selectedIndex+ " correct:"+selectedCorrect);

                var result    = $(manager.templateResultFalse({ }));
                if(selectedCorrect)
                {
                    result    = $(manager.templateResultCorrect({ }));
                    manager.ix++;
                    manager._saveState();
                }

                $('#result').html(result);
                $('#quiz').fadeOut(200,function() {
                    $('#result').show();
                });
                setTimeout(function(){
                    $('#result').fadeOut(200,function() {
                        manager.createQuiz();
                    });
                }, 500);

            });
            $("#quiz").append(lable);
        }
    }

    this.build = function()
    {
         this.createQuiz();
    }

}