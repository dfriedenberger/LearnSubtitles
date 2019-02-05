

CardManager = function(language,bucketId)
{
    this.dataset = [];
    this.ix = 0;
    this.language = language;
    this.bucketId = bucketId;

    $('.play-command-next').hide(0);

    //init
    var dataStr = localStorage.getItem(this.bucketId);
    if(dataStr)
    {
        var data = JSON.parse(dataStr);
        this.ix = data.ix;
    }


    this.setDataset = function(dataset)
    {
        this.dataset = dataset;
    }


    this._sendState = function(pos, nivel)
    {
        console.log(pos+' '+nivel);
    }

    this._timestr = function(millis)
    {
        var ss = parseInt(millis / 1000);
        var hh =  parseInt(ss / 3600);
        ss -= hh * 3600;
        var mm =   parseInt(ss / 60);
        ss -= mm * 60;

       return (hh > 9?hh:"0"+hh) + ":" + (mm > 9?mm:"0"+mm) + ":" + (ss > 9?ss:"0"+ss);
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


        $('.statistic').text( (this.ix +1) +"/"+this.dataset.length);
        $('.time').text(this._timestr(list[0].from));
        $.each( list , function( key, value ) {
        
            if(value.lang == manager.language)
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

    
    this._nextCard = function()
    {
        this.ix++;

        var data = { ix : this.ix };
        localStorage.setItem(this.bucketId, JSON.stringify(data));	

        this._showCard();
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
            ev.preventDefault();
            manager._nextCard();
        });


        manager._showCard();

			
		    
    }

}