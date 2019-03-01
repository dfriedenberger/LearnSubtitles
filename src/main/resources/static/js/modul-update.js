
State = function(container)
{
    this.container = container;
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



processStart = function(bucketId)
{
    state.info("Start");

    var form = $("#metadata");

    var bucketCommand = {
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
                state.done("Ok")
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
        console.log("send form of ",bucket);
        processStart(bucket);
      
    });

    $("#files").find("a").click(function(ev) {
        ev.preventDefault();    
        var href = $(this).attr("href");
        var row = $(this).closest("tr");
        console.log("call",href,row);
        $.ajax({
            type: "GET",
            url: href,
            cache: false,
            success: function(data)
            {
                $(row).slideUp("normal", function() { $(this).remove(); } );
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log("Server response "+xhr.status);
            }
        });
    });
});
