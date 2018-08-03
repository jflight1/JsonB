function DayReadingController($scope, $http) {

  // read query params
  var queryParams = _.object(_.compact(_.map(location.search.slice(1).split('&'), function(item) {  if (item) return item.split('='); })));
  var queryParamKeys = Object.keys(queryParams);

  var month = queryParams.month;
  if (month.length == 1) {
    month = "0" + month;
  }

  var bOldTestament = queryParamKeys.includes("old");
  var bNewTestament = queryParamKeys.includes("new");
  var bPsalms = queryParamKeys.includes("psalms");
  var bProverbs = queryParamKeys.includes("proverbs");
  var bPsalmsAndProverbs = queryParamKeys.includes("psalmsAndProverbs");

  // get the json data
  $http.get("../json/day_reading/" + month + ".json").then(function(response) {
    $scope.dayReadings = response.data;

    // for each dayReading, we add an array of the dayReadingSections we want to show.
    for (var i = 0; i < $scope.dayReadings.length; i++) {
      var dayReading = $scope.dayReadings[i];

      dayReading.dayReadingSections = [];
      var j = 0;
      if (bOldTestament) {
        dayReading.dayReadingSections[j++] = dayReading.oldTestament;
      }
      if (bNewTestament) {
        dayReading.dayReadingSections[j++] = dayReading.newTestament;
      }
      if (bPsalms) {
        dayReading.dayReadingSections[j++] = dayReading.psalms;
      }
      if (bProverbs) {
        dayReading.dayReadingSections[j++] = dayReading.proverbs;
      }
      if (bPsalmsAndProverbs) {
        dayReading.dayReadingSections[j++] = dayReading.psalmsAndProverbs;
      }
    }

  });

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
