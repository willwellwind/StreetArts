
function commonReady(){
var a = new Date();
var b = a.getHours();
if ( b<7 || b >= 19){
O("background").src="night_background.svg";
S('body').backgroundColor='#3c96d2';

}

}

function getQueryVariable(variable)
{
       var query = window.location.search.substring(1);
       var vars = query.split("&");
       for (var i=0;i<vars.length;i++) {
               var pair = vars[i].split("=");
               if(pair[0] == variable){return pair[1];}
       }
       return(false);
}

