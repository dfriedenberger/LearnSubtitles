$( document ).ready(function() {

    console.log( "ready!" );

    var source   = document.getElementById("row-template").innerHTML;
    var template = Handlebars.compile(source);
    
    $("#checkbox-all").click(function() {
        var checked = $(this).prop('checked');
        $(".checkbox").prop('checked', checked);
    });
    
    var cycle = new Cycle(1000);

    CallCommand = function(href)
    {
        $.ajax({
            type: "GET",
            url: href,
            cache: false,
            success: function(data)
            {
               console.log(data);
               cycle.enable();
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log("Server response "+xhr.status);
            }
        });
        refreshJobsList = true;

    }

    cycle.setCallback( function()
    {

        $.ajax({
            type: "GET",
            url: "/api/v1/composer",
            cache: false,
            success: function(data)
            {
               cycle.disable();
               console.log(data);
               $.each( data, function( key, value ) {
                   console.log(value);
                    switch(value.state)
                    {
                       case "init":
                       case "running":
                       cycle.enable();
                       break;
                       case "done":
                       case "exception":
                        break;
                       default:
                        console.log("Unknown state",value);
                        break;
                    }
               });
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log("Server response "+xhr.status);
            }
        });
    });

    cycle.enable();
    cycle.start();

    $("#generate-group").find("a").click(function(ev) {
        ev.preventDefault();
        var command = $(this).data('command');
        //console.log("call command",command);
        $(".checkbox").each(function() {
            var checked = $(this).prop('checked');
            var a = $(this).closest("tr").find("[data-command='"+command+"']");
            if(checked)
            {
                CallCommand(a.attr("href"))
            }
        });

    });

   
   


  $.ajax({
       url: "/api/v1/buckets",
       context : this,
    }).then(function(dataset) {
       console.log(dataset);
       $.each( dataset, function( key, value ) {


            var state = {
                 srt         : 0,
                 data        : "red",
                 zip         : 0,
                 archive     : "grey",
                 info        : "red",
                 image       : "red",
                 merge       : "grey",
                 translation : "grey",
                 error       : ""
             }
            var files = "";
            if(value.files)
            {
                console.log("files",value.files);
                files = value.files.join("<br>");
                var  arrayLength = value.files.length;
                for (var i = 0; i < arrayLength; i++) {
                    var filename  = value.files[i];

                    if(filename.endsWith(".srt")) 
                    {
                        state.srt++;
                        state.data = state.srt == 2 ? "green" : "red";
                        continue;
                    }
                    if(filename.endsWith(".zip")) 
                    {
                        state.zip++;
                        state.archive = "blue";
                        continue;
                    }
                    if(filename.endsWith(".jpg")) 
                    {
                        state.image = "green";
                        continue;
                    }
                    // info.json, merge_gen.txt, translation_gen.json
                    if(filename === ("info.json")) 
                    {
                        state.info = "green";
                        continue;
                    }
                    if(filename === ("merge_gen.txt")) 
                    {
                        state.merge = "green";
                        continue;
                    }
                    if(filename === ("translation_gen.json")) 
                    {
                        state.translation = "green";
                        continue;
                    }
                    state.error = "unknown file "+filename;
                }
            }
            else
            {
                state.error = "no files";
            }

           var context = { 
                  id : value.id,
                  name: value.name, 
                  state : state,
                  files : files
           };
        
           var html    = template(context);
           $("#rows").append(html);
       
       
       });

       $(".generate-item").click(function(ev) {
            ev.preventDefault();
            var href = $(this).attr('href');
            CallCommand(href);
       });
    })		    
});