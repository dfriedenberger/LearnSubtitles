
State = function(container)
{
    this.container = container;
    this.set = function(s)
    {
        switch(s)
        {
           case "0": //running
            this.info("running ...");
            break;
          case "99": //ready
            this.done("ready");
            break;
          default:
            this.error("state ="+s);
            break;
        }
    };
    this.info = function(text)
    {
        $(this.container).addClass("alert-primary");
        $(this.container).text(text);
    };
    this.error = function(text)
    {
        $(this.container).addClass("alert-danger");
        $(this.container).text(text);
    };
    this.done = function(text)
    {
        $(this.container).addClass("alert-success");
        $(this.container).text(text);
    };
}

var state = new State("#status .alert");


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
                state.set(data.state);
                if(data.state == 0)
                    processState(bucketId);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                state.error("Server response "+xhr.status);
            }
        })
    },1000);

}

processStart = function(bucketId)
{
    state.info("Start");

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
            },
            error: function (xhr, ajaxOptions, thrownError) {
                state.error("Server response "+xhr.status);
            }
    });
}




$( document ).ready(function() {

    $("#process").find("a").click(function(ev) {
        ev.preventDefault();    
        var bucket = $(this).data("bucket");
        console.log("process "+bucket);
        $(this).prop("disabled",true);
        processStart(bucket);
      
    });

});