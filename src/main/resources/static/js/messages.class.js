Messages = function(url)
{
    this.messages = [];
    this.url = url;
    this.load = function()
    {
        var d = $.Deferred();
        $.ajax({
            url: this.url,
            context : this,
            success: function (data){
                    var lines = data.split("\n");
                    var id;
                    for (var i = 0, len = lines.length; i < len; i++) {
                        var msg = /([a-z]+)\s+\"(.*)\"/.exec(lines[i]);
                        if(msg)
                        {
                            if(msg[1] == "msgid")
                            {
                                id = msg[2];
                            }
                            if(msg[1] == "msgstr")
                            {
                                this.messages[id] = msg[2];
                            }
                        }
                    }
                    console.log(this.messages);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                    console.log(xhr.status);
                    console.log(thrownError);
            },
            complete: function()
            {
                d.resolve();
            }
        });
        return d;
     }
     this.exists = function(key)
     {
         return key in this.messages;
     }
     this.get = function(key)
     {
         return this.messages[key];
     }

}