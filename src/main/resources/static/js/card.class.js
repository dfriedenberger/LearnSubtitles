
   
CardManager = function(language0,language1,bucketId)
{
    this.dataset = [];
    this.ix = 0;
    this.language0 = language0;
    this.language1 = language1;
    this.bucketId = bucketId;
    this.database = new Database();

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

        var list = this.dataset[this.ix];
    
        //cleanup
        $('.tr-box').empty();
        $('.tx-box').empty();
        $('.play-command-next').hide(0);
        $('.play-command-show').show(0);

        var percent = parseInt(100 * this.ix  / this.dataset.length);

        $('.statistic').text( (this.ix +1) +"/"+this.dataset.length+" ("+percent+"%)");
        $.each( list , function( key, value ) {
        
            if(value.lang == manager.language1)
            {
                container = $('.tr-box');
            }
            else
            {
                container = $('.tx-box');
            }
            
            $.each( value.text, function( key, value ) {
                container.append("<span>"+value+"</span> ");
            });	

        });

        $('.tr-box').find("span").hide(0);

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

            $('.tr-box').find("span").fadeIn(500);
            $('.play-command-show').hide(0);
            $('.play-command-next').show(0);

        });
        
        $('.play-command-next').click(function(ev){
            this.ix++;

            ev.preventDefault();
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