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
    {lineName: "L6", x: 160, y: 175},
    {lineName: "L7", x: 160, y: 90},
    {lineName: "L8", x: 510, y: 160},
    {lineName: "L9", x: 510, y: 290}
//    {lineName: "CELL", x: 980, y: 185}
];
var testGroup = [
    {people: 1, x: 535, y: 95}, // group 40
    {people: 1, x: 535, y: 50}, // group 39
    {people: 1, x: 535, y: 10}, // group 38
    {people: 1, x: 625, y: 95}, // group 37
    {people: 1, x: 625, y: 50}, // group 36
    {people: 1, x: 625, y: 5}, // group 35
    {people: 5, x: 590, y: 180}, // group 30-34
    {people: 5, x: 595, y: 275} // group 25-29
];
var babGroup = [
    {people: 8, x: 770, y: 260, lineName: "L1"},
    {people: 8, x: 770, y: 165, lineName: "L2"},
    {people: 7, x: 770, y: 105, lineName: "L3"},
    {people: 7, x: 770, y: 10, lineName: "L4"},
    {people: 3, x: 220, y: 180, lineName: "L6"},
    {people: 3, x: 230, y: 110, lineName: "L7"},
    {people: 4, x: 365, y: 175, lineName: "L8"},
    {people: 4, x: 365, y: 275, lineName: "L9"}
];

var maxTestTableNo = 40;
var sitefloor = 6;

