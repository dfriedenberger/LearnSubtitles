

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

    
    this._nextCard = function()
    {
        this.ix++;
        var data = { 
            ix : this.ix,
            language0 : this.language0,
            language1 : this.language1
        };
        this.database.write(this.bucketId,data);

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