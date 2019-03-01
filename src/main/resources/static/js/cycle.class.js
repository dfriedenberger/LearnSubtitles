Cycle = function(timeout)
{
    this.callback = function() { console.log("callback not set")};
    this.timeout = timeout;
    this.enabled = false;

    this.setCallback = function(callback)
    {
        this.callback = callback;
    }
    this.cycle = function()
    {
        var that = this;
        //console.log("cycle",this.enabled);
        if(this.enabled)
        {
            this.callback();
        }
        setTimeout(function() { that.cycle(); },this.timeout);
    }

    this.start = function()
    {
        this.cycle();
    }
    this.enable = function()
    {
        this.enabled = true;
    }
    this.disable = function()
    {
        this.enabled = false;
    }
}
