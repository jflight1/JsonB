function VersesWithNotesController($scope, $http) {

    // read query params
    var queryParams = _.object(_.compact(_.map(location.search.slice(1).split('&'), function(item) {  if (item) return item.split('='); })));
    var queryParamKeys = Object.keys(queryParams);

    var month = queryParams.month;
    if (month.length == 1) {
        month = "0" + month;
    }

    var day = queryParams.day;
    if (day.length == 1) {
        day = "0" + day;
    }

    var section;
    if (queryParamKeys.includes("old")) {
        section = "old"
    }
    else if (queryParamKeys.includes("new")) {
        section = "new"
    }
    else if (queryParamKeys.includes("psalms")) {
        section = "psalms"
    }
    else if (queryParamKeys.includes("proverbs")) {
        section = "proverbs"
    }

    // get the json data
    $http.get("../json/verses_with_notes/" + month + "/" + day + "_" + section + ".json").then(function(response) {
        $scope.versesWithNotess = response.data;
    });

    $scope.clickButton = function() {
        for (var i = 0; i < $scope.versesWithNotess.length; i++) {
            var versesWithNotes = $scope.versesWithNotess[i];

            var rsbNotes = versesWithNotes.rsbNotes;
            for (var j = 0; j < rsbNotes.length; j++) {
                var rsbNote = rsbNotes[j];
                var td = document.getElementById("td_" + rsbNote.id);
                td.innerHTML = rsbNote.text;

                td.childNodes = td.childNodes[0].childNodes[0].childNodes;
            }
        }
    };

    /*
     [ {
     "month" : 1,
     "day" : 1,
     "oldTestament" : {
     "verseRange" : "genesis,1,1-genesis,2,25",
     "url" : "https://www.biblegateway.com/passage/?search=genesis+1%3A1-2%3A25",
     "displayString" : "Genesis 1:1-2:25"
     },
     "newTestament" : {
     "verseRange" : "matthew,1,1-matthew,2,12",
     "url" : "https://www.biblegateway.com/passage/?search=matthew+1%3A1-2%3A12",
     "displayString" : "Matthew 1:1-2:12"
     },
     "psalms" : {
     "verseRange" : "psalms,1,1-psalms,1,6",
     "url" : "https://www.biblegateway.com/passage/?search=psalms+1%3A1-1%3A6",
     "displayString" : "Psalms 1:1-6"
     },
     "proverbs" : {
     "verseRange" : "proverbs,1,1-proverbs,1,6",
     "url" : "https://www.biblegateway.com/passage/?search=proverbs+1%3A1-1%3A6",
     "displayString" : "Proverbs 1:1-6"
     }
     }, { ....}, ...
     ]

     */

}
