jQuery.fn.highlight = function (str, text) {
    var regex = new RegExp(str, "gi");
    return this.each(function () {
        this.innerHTML = this.innerHTML.replace(regex, function(matched) {

            return '<span class="highlight" data-title="'+text+'">'+matched+'</span>';

        });
    });
};


CardText = function(token)
{
    this.token = token;
    this.annotation = undefined;
    this.setAnnotation = function(annotation)
    {
        this.annotation = annotation;
    }
    this._needSpace = function(c)
    {
        switch(c)
        {
            case ".":
            case "!":
            case "?":
            case ",":
                return false;
            default:
                return true;
        }



    }
    this.toHtml = function()
    {
        var text = "";
        if(this.annotation)
        {
            text += '<span class="highlight" data-title="'+this.annotation+'">';
        }
        for(var i = 0;i < this.token.length;i++)
        {
          var t = this.token[i];

          if(this._needSpace(t))
          {
            if(i == 0)
              text = " "+text;
            else
                text += " ";
          }
          
          

          text += t;
        }
        if(this.annotation)
        {
            text += '</span>';
        }
        return text;
    }
}

CardManager = function(database,language0,language1,bucketId)
{
    this.dataset = [];
    this.ix = 0;
    this.language0 = language0;
    this.language1 = language1;
    this.bucketId = bucketId;
    this.database = database;

    $('.play-command-next').hide(0);

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

        var list = this.dataset.cardSets[this.ix];

        console.log(list);

        //cleanup
        $('.tr-box').empty();
        $('.tx-box').empty();
        $('.play-command-next').hide(0);
        $('.play-command-show').show(0);

        var i = parseInt(this.ix);
        var percent = parseInt(100 *  i / this.dataset.cardSets.length);

        $('.statistic').text( (i + 1 ) +"/"+this.dataset.cardSets.length+" ("+percent+"%)");


        var l1 = list.cards[manager.language0];
        console.log(l1);


        var html1 = "";
        $(l1.textParts).each(function(w,word) {

            console.log(word);
            var text = new CardText(word.token);
            console.log(text.toHtml());

            if(word.annotationId)
            {
                var annotation = l1.annotations[word.annotationId];                
                text.setAnnotation(annotation);
            }
            html1 += text.toHtml();
        });
        $('.tx-box').append("<p>" + html1 + "</p>");


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


        var l2 = list.cards[manager.language1];
        console.log(l2.textParts);


        var html2 = "";
        $(l2.textParts).each(function(w,word) {
            html2 += new CardText(word.token).toHtml();
        });
        $('.tr-box').append("<p>" + html2 + "</p>");      
        $('.tr-box').find("p").hide(0);

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
            
		$('.play-command-show').click(function(ev){
            ev.preventDefault();

            $('.tr-box').find("p").fadeIn(500);
            $('.play-command-show').hide(0);
            $('.play-command-next').show(0);

        });
        
        $('.play-command-next').click(function(ev){
            ev.preventDefault();

            manager.ix++;
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