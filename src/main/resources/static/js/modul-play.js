

$( document ).ready(function() {

    /* disable Screensaver */
    var noSleep = new NoSleep();
    var wakeLockEnabled = false;
 
   var setDataText = function(item,text) {
       var text = $(item).data(text);
       $(item).text(text);
   }
   
   //init
    $(".screensaver-button").each(function() {
        setDataText(this,"text-disable");
    });
    $(".screensaver-icon").addClass("invisible");

    $(".screensaver").click( function(ev) {
    ev.preventDefault();
      if (!wakeLockEnabled) {
        noSleep.enable(); // keep the screen on!
        wakeLockEnabled = true;

        $(".screensaver-icon").addClass("blink");
        $(".screensaver-icon").removeClass("invisible");
        $(".screensaver-button").each(function() {
            setDataText(this,"text-enable");
        });

      } else {
        noSleep.disable(); // let the screen turn off.
        wakeLockEnabled = false;
        $(".screensaver-icon").removeClass("blink");
        $(".screensaver-icon").addClass("invisible");
        $(".screensaver-button").each(function() {
            setDataText(this,"text-disable");
        });

      }
    });

    
});
