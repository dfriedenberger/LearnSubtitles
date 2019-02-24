
Database = function(sync)
{
    this.sync = sync;
    this._version = 1;

    this.sync = function(key) 
    {
        var d = $.Deferred();
        
        if(this.sync) 
        {
            $.ajax({
                type: "GET",
                url: "/api/v1/database/"+key,
                cache: false,
                contentType: "application/json",
                success: function(data)
                {
                    console.log(data);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log("Server response",xhr,thrownError);
                },
                complete: function()
                {
                    d.resolve();
                }
            });
        }
        else
        {
            console.log("no sync available");
            d.resolve();
        }

        return d;
    }
    this.read = function(key)
    {
        var dataStr = localStorage.getItem(key);
        console.log("read "+key+" = "+dataStr);
        if(!dataStr) return undefined;
        var data = JSON.parse(dataStr);
        if(data.version != this._version)
            return undefined;
        return data;
    }

    this.write = function(key,data)
    {
        data.version = this._version;
        var dataStr = JSON.stringify(data);
        console.log("write "+key+" = "+dataStr);
        localStorage.setItem(key, dataStr);	

        console.log("sync " + this.sync);

        if(!this.sync) return;

        $.ajax({
            type: "PUT",
            url: "/api/v1/database/"+key,
            cache: false,
            contentType: "application/json",
            data : JSON.stringify(data),
            success: function(data)
            {
                console.log(data);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log("Server response",xhr,thrownError);
            }
        });



    }

}
