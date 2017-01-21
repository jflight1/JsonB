function DayReadingController($scope, $http) {

    $scope.dayReadingRows = [
        {
            "date": "1/1",
            "links": [
                {
                    "href": "https://www.biblegateway.com/passage/?search=proverbs+7%3A1-7%3A5",
                    "text": "tttttttttttt"
                }
            ]
        },
        {
            "date": "1/2",
            "links": [
                {
                    "href": "https://www.biblegateway.com/passage/?search=proverbs+7%3A1-7%3A5",
                    "text": "ttttt22222222"
                }
            ]
        }

    ];


    $http.get("../../../json/day_reading/01.json").then(function(response) {
        var json = response.data;
        for (var i = 0; i < response.data.length; i++) {
            var dayReadingJson = json[i];
            $scope.dayReadingRows[i] = {
                "date": "" + dayReadingJson.month + "/" + dayReadingJson.day,
                "links": [
                    {
                        "href": "https://www.biblegateway.com/passage/?search=proverbs+7%3A1-7%3A5",
                        "text": "tttttttttttt"
                    },
                    {
                        "href": "https://www.biblegateway.com/passage/?search=proverbs+7%3A1-7%3A5",
                        "text": "22222222"
                    }
                ]
            };
        }

    });

    /*

     {
     "month" : 1,
     "day" : 2,
     "oldTestament" : "genesis,3,1-genesis,4,26",
     "newTestament" : "matthew,2,13-matthew,3,6",
     "psalms" : "psalms,2,1-psalms,2,12",
     "proverbs" : "proverbs,1,7-proverbs,1,9"
     }
     */

}
