var pXa = -0;
var pYa = -0;

var mapInfo = {
    titleName: "東湖廠5樓",
    x: 50,
    y: 400
};

var titleGroup = [
    {lineName: "LA", x: 1070, y: 50},
    {lineName: "LB", x: 1070, y: 160},
    {lineName: "LC", x: 1070, y: 230},
    {lineName: "LD", x: 1070, y: 300},
    {lineName: "LH", x: 40, y: 120},
    {lineName: "LG", x: 370, y: 80},
    {lineName: "LF", x: 370, y: 160}
];

var testGroup = [
    {people: 1, x: 490, y: 70}, // group 24
    {people: 1, x: 490, y: 150}, // group 23
    {people: 7, x: 530, y: 70}, // group 16-22
    {people: 7, x: 530, y: 150}, // group 9-15
    {people: 4, x: 620, y: 190}, // group 5-8
    {people: 4, x: 620, y: 275} // group 1-4
];

var babGroup = [
    {people: 4, x: 930, y: 60, lineName: "LA"},
    {people: 4, x: 930, y: 170, lineName: "LB"},
    {people: 4, x: 930, y: 220, lineName: "LC"},
    {people: 4, x: 930, y: 310, lineName: "LC"},
    {people: 3, x: 100, y: 160, lineName: "LH"},
    {people: 3, x: 260, y: 90, lineName: "LG"},
    {people: 3, x: 260, y: 160, lineName: "LF"}
];

var maxTestTableNo = 24;
var sitefloor = 5;