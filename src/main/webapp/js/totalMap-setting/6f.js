var pXa = 0;
var pYa = 0;

var mapInfo = {
    titleName: "東湖廠6樓",
    x: 30,
    y: 395
};

var titleGroup = [
    //assy
    {lineName: "L1", x: 1055, y: 260},
    {lineName: "L2", x: 1055, y: 155},
    {lineName: "L3", x: 1055, y: 90},
    {lineName: "L4", x: 1055, y: 0},
    //pkg
    {lineName: "L6", x: 310, y: 195},
    {lineName: "L7", x: 310, y: 100},
    {lineName: "L8", x: 510, y: 160},
    {lineName: "L9", x: 510, y: 290}
//    {lineName: "CELL", x: 980, y: 185}
];
var testGroup = [
    {people: 4, x: 545, y: 15}, // group 39-42
    {people: 4, x: 545, y: 105}, // group 35-38
    {people: 5, x: 590, y: 180}, // group 30-34
    {people: 5, x: 595, y: 275} // group 25-29
];
var babGroup = [
    {people: 8, x: 770, y: 260, lineName: "L1"},
    {people: 8, x: 770, y: 165, lineName: "L2"},
    {people: 7, x: 770, y: 105, lineName: "L3"},
    {people: 7, x: 770, y: 10, lineName: "L4"},
    {people: 3, x: 205, y: 180, lineName: "L6"},
    {people: 3, x: 205, y: 105, lineName: "L7"},
    {people: 4, x: 365, y: 175, lineName: "L8"},
    {people: 4, x: 365, y: 275, lineName: "L9"}
];

var maxTestTableNo = 42;
var sitefloor = 6;

