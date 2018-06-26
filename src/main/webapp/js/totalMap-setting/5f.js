var pXa = -0;
var pYa = -0;

var mapInfo = {
    titleName: "東湖廠5樓",
    x: 30,
    y: 395
};

var titleGroup = [
    //assy
    {lineName: "LA", x: 1070, y: 50},
    {lineName: "LB", x: 1070, y: 160},
    //pkg
    {lineName: "LF", x: 365, y: 155},
    {lineName: "LG", x: 365, y: 50},
    {lineName: "LH", x: 15, y: 120},
    {lineName: "FQC", x: 210, y: 240}
];

var testGroup = [
    {people: 2, x: 680, y: 30}, // group 19-20
    {people: 9, x: 450, y: 75}, // group 10-18
    {people: 9, x: 450, y: 160} // group 1-9
];

var babGroup = [
    {people: 7, x: 820, y: 60, lineName: "LA"},
    {people: 7, x: 820, y: 170, lineName: "LB"},
    {people: 3, x: 250, y: 155, lineName: "LF"},
    {people: 3, x: 250, y: 70, lineName: "LG"},
    {people: 3, x: 90, y: 160, lineName: "LH"}
];

var fqcGroup = [
    {people: 1, x: 300, y: 215, lineName: "FQC_3"},
    {people: 1, x: 300, y: 255, lineName: "FQC_4"},
    {people: 1, x: 300, y: 300, lineName: "FQC_5"}
];

var maxTestTableNo = 20;
var sitefloor = 5;