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
    {lineName: "LF", x: 365, y: 155},
    {lineName: "LG", x: 365, y: 50},
    {lineName: "LH", x: 95, y: 200},
    {lineName: "FQC", x: 210, y: 240}
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
    {people: 3, x: 250, y: 155, lineName: "LF"},
    {people: 3, x: 250, y: 70, lineName: "LG"},
    {people: 3, x: 90, y: 160, lineName: "LH"}
];

var fqcGroup = [
    {people: 1, x: 300, y: 215, lineName: "FQC_3"},
    {people: 1, x: 300, y: 255, lineName: "FQC_4"},
    {people: 1, x: 300, y: 300, lineName: "FQC_5"}
];

var maxTestTableNo = 18;
var sitefloor = 5;