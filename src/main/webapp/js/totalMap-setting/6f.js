var pXa = 0;
var pYa = 0;

var mapInfo = {
    titleName: "東湖廠6樓",
    x: 555,
    y: 395
};

var titleGroup = [
    //assy
    {lineName: "L1", x: 1055, y: 260},
    {lineName: "L2", x: 1055, y: 155},
    {lineName: "L3", x: 1055, y: 90},
    {lineName: "L4", x: 1055, y: 0},
    //pkg
    {lineName: "L6", x: 520, y: 285},
    {lineName: "L7", x: 160, y: 90},
    {lineName: "L8", x: 425, y: 155},
    {lineName: "L9", x: 340, y: 240},
    {lineName: "FQC", x: 320, y: 400}
];

var testGroup = [
    {people: 3, x: 420, y: 100}, // group 40-42
    {people: 3, x: 420, y: 30}, // group 37-39
    {people: 1, x: 550, y: 115}, // group 36
    {people: 1, x: 550, y: 70}, // group 35
    {people: 1, x: 550, y: 30}, // group 34
    {people: 1, x: 600, y: 115}, // group 33
    {people: 1, x: 600, y: 70}, // group 32
    {people: 1, x: 600, y: 30}, // group 31
    {people: 3, x: 765, y: 115}, // group 28-30
    {people: 3, x: 765, y: 20} // group 25-27
];

var babGroup = [
    {people: 4, x: 900, y: 265, lineName: "L1"},
    {people: 4, x: 900, y: 185, lineName: "L2"},
    {people: 4, x: 900, y: 110, lineName: "L3"},
    {people: 4, x: 900, y: 20, lineName: "L4"},
    {people: 3, x: 565, y: 245, lineName: "L6"},
    {people: 3, x: 235, y: 100, lineName: "L7"},
    {people: 3, x: 510, y: 170, lineName: "L8"},
    {people: 3, x: 400, y: 245, lineName: "L9"}
];

var fqcGroup = [
    {people: 1, x: 410, y: 370, lineName: "FQC_1"},
    {people: 1, x: 410, y: 440, lineName: "FQC_2"}
];

var maxTestTableNo = 42;
var sitefloor = 6;

