



process = function(bucket)
{
    setTimeout(function(){
        $.ajax({
                type: "GET",
                url: '/process/'+bucket,
                cache: false,
                success: function(data)
                {
                   console.log(data);
                   if(data.message == "ok")
                   {
                     //weiter
                     process(bucket);
                   }
                }
       });
    }, 1000);	
}




$( document ).ready(function() {

    $("#process").find("button").click(function(ev) {
        ev.preventDefault();    
        var bucket = $(this).data("bucket");
        console.log("process "+bucket);

        process(bucket);
      
    });

});