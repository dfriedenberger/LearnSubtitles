

processState = function(bucketId)
{
    setTimeout(function(){
        $.ajax({
            type: "GET",
            url: '/api/v1/create/'+bucketId,
            cache: false,
            success: function(data)
            {
                console.log(data);
                if(data.state == 0)
                    processState(bucketId);
            }
        })
    },1000);

}

processStart = function(bucketId)
{
   
    var form = $("#metadata");
    console.log(form.serialize());

    var bucketCommand = {
        id : form.find('input[name="id"]').val(),
        title : form.find('input[name="title"]').val(),
        description : form.find('textarea[name="description"]').val()
    };

    $.ajax({
            type: "PUT",
            url: form.attr('action'),
            cache: false,
            contentType: "application/json",
            data : JSON.stringify(bucketCommand),
            success: function(data)
            {
                console.log(data);
                //weiter
                processState(bucketId);
            }
    });
}




$( document ).ready(function() {

    $("#process").find("button").click(function(ev) {
        ev.preventDefault();    
        var bucket = $(this).data("bucket");
        console.log("process "+bucket);

        processStart(bucket);
      
    });

});