
Database = function()
{
    this._version = 1;
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
    }

}
