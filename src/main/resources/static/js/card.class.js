jQuery.fn.highlight = function (str, text) {
    var regex = new RegExp(str, "gi");
    return this.each(function () {
        this.innerHTML = this.innerHTML.replace(regex, function(matched) {

            return '<span class="highlight" data-title="'+text+'">'+matched+'</span>';

        });
    });
};



   
      

CardManager = function(database,messages,language0,language1,bucketId, resetCallback)
{
    this.dataset = [];
    this.ix = 0;
    this.language0 = language0;
    this.language1 = language1;
    this.bucketId = bucketId;
    this.database = database;
    this.messages = messages;
    this.resetCallback = resetCallback;


    //init
    var data = this.database.read(this.bucketId);
    if(data)
    {

        this.ix = data.ix;
        this.language0 = data.language0;
        this.language1 = data.language1;
    }
   
    this.setDataset = function(dataset)
    {
        this.dataset = dataset;
    }


    this._sendState = function(pos, nivel)
    {
        console.log(pos+' '+nivel);
    }

  

    this._showCard = function()
    {
        var manager = this;

        var card = this.dataset[this.ix];

        console.log(card);

        //cleanup
        $('.tr-box').empty();
        $('.tx-box').empty();
        manager.resetCallback();


        var i = parseInt(this.ix);
        var percent = parseInt(100 *  i / this.dataset.length);

        $('.statistic').text( (i + 1 ) +"/"+this.dataset.length);


        console.log(card.text);
        var html = card.text;

        var ix = 0;
        console.log(card.annotations);
        for(var i = 0;i < card.annotations.length;i++)
        {
            console.log(card.annotations[i]);
            if(card.annotations[i].level < 9) continue;
            var key = card.annotations[i].key;

            var s = html.indexOf(key,ix);
            console.log(key + " found on pos "+s+" in "+html);
            if(s < 0) continue; // not found, fehler
            var e = s + key.length;
            //Todo check if correct word was found 
            //if((n == 0 ||  word[n] == ' ')) && (n + word.length)
            
            if(messages.exists(key))
            {
                var tag = '<span class="highlight" data-title="'+messages.get(key)+'">' + key + '</span>';
                html = html.substring(0,s) +  tag + html.substring(e);
                ix = s + tag.length;
            }
            else
            {
              ix = s + 1; //next
            }
        }

        $('.tx-box').append("<p>" + html + "</p>");


        $('.highlight').each(function(k,val) {
            var data = $(val).data("title");

            new Tooltip($(val), {
                placement: 'bottom', // or bottom, left, right, and variations
                title: data,
                html: true,
                container: "body",
                template: '<div class="tooltip1" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
            })

        });

        $('.tr-box').append("<p>" + card.translation + "</p>");      
        
    }
    this._toFlag = function(lang) {
        switch(lang)
        {
        case 'en':
            return 'us';
        default:
            return lang;
        }
    }
    this._showFlags = function()
    {
        var flags = $('.change-language').find("img").toArray();
        console.log(flags);
        $(flags[0]).removeClass("flag-"+this._toFlag(this.language1));
        $(flags[0]).addClass("flag-"+this._toFlag(this.language0));
        $(flags[1]).removeClass("flag-"+this._toFlag(this.language0));
        $(flags[1]).addClass("flag-"+this._toFlag(this.language1));
    }

    this._saveState = function()
    {
        var data = { 
            ix : this.ix,
            language0 : this.language0,
            language1 : this.language1
        };
        this.database.write(this.bucketId,data);

    }

    this.build = function()
    {
 	    var manager = this;
            
        
        $('.play-command-next').click(function(ev){
            ev.preventDefault();

            manager.ix++;
            manager._saveState();
            manager._showCard();

        });
        $('.play-command-back').click(function(ev){
            ev.preventDefault();

            manager.ix--;
            manager._saveState();
            manager._showCard();

        });
        $('.change-language').click(function(ev){
            ev.preventDefault();

            //change langauges
            var tmp = manager.language0;
            manager.language0 = manager.language1;
            manager.language1 = tmp;

            manager._saveState();
            manager._showFlags();
            manager._showCard();
        });

        manager._showFlags();
        manager._showCard();

			
		    
    }

}