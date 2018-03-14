var pXa = -0;
var pYa = -0;

var mapInfo = {
    titleName: "東湖廠5樓",
    x: 30,
    y: 395
};

var titleGroup = [
    {lineName: "LA", x: 1070, y: 50},
    {lineName: "LB", x: 1070, y: 160},
//    {lineName: "LC", x: 1070, y: 230},
//    {lineName: "LD", x: 1070, y: 300},
    {lineName: "LH", x: 15, y: 120},
    {lineName: "LG", x: 365, y: 50},
    {lineName: "LF", x: 365, y: 155}
];

var testGroup = [
    {people: 2, x: 680, y: 30}, // group 19-20
    {people: 9, x: 450, y: 75}, // group 10-18
    {people: 9, x: 450, y: 160} // group 1-9
];

var babGroup = [
    {people: 7, x: 820, y: 60, lineName: "LA"},
    {people: 7, x: 820, y: 170, lineName: "LB"},
//    {people: 8, x: 790, y: 220, lineName: "LC"},
//    {people: 8, x: 790, y: 310, lineName: "LD"},
    {people: 3, x: 90, y: 160, lineName: "LH"},
    {people: 3, x: 250, y: 70, lineName: "LG"},
    {people: 3, x: 250, y: 155, lineName: "LF"}
];

var maxTestTableNo = 20;
var sitefloor = 5;