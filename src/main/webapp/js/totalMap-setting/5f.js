var pXa = -0;
var pYa = -0;

var mapInfo = {
    titleName: "東湖廠5樓",
    x: 30,
    y: 395
};

var titleGroup = [
    //assy
    {lineName: "LA", x: 1090, y: 50},
    {lineName: "LB", x: 1090, y: 160},
    {lineName: "LD", x: 730, y: 160},
//    {lineName: "LE", x: 985, y: 420},
    //pkg
    {lineName: "LF", x: 300, y: 40},
    {lineName: "LG", x: 210, y: 40},
    {lineName: "LH", x: 125, y: 40}
//    {lineName: "FQC", x: 210, y: 240}
];

var testGroup = [
    {people: 8, x: 500, y: 35}, // group 11-18
    {people: 5, x: 500, y: 85}, // group 6-10
    {people: 5, x: 500, y: 165} // group 1-5
];

var babGroup = [
    {people: 7, x: 825, y: 60, lineName: "LA"},
    {people: 4, x: 930, y: 170, lineName: "LB"},
    {people: 3, x: 795, y: 170, lineName: "LD"},
//    {people: 4, x: 1045, y: 440, lineName: "LE"},
    {people: 3, x: 320, y: 100, lineName: "LF", straight: true, reverse: true},
    {people: 3, x: 230, y: 100, lineName: "LG", straight: true, reverse: true},
    {people: 3, x: 160, y: 100, lineName: "LH", straight: true, reverse: true}
];

var fqcGroup = [
//    {people: 1, x: 300, y: 215, lineName: "FQC_3"},
//    {people: 1, x: 300, y: 255, lineName: "FQC_4"},
//    {people: 1, x: 300, y: 300, lineName: "FQC_5"}
];

var maxTestTableNo = 18;
var sitefloor = 5;