var pXa = -20;
var pYa = -20;

var mapInfo = {
    titleName: "東湖廠6樓",
    x: 950,
    y: 50
};

var titleGroup = [
    //assy
    {lineName: "L1", x: 810, y: 265},
    {lineName: "L2", x: 810, y: 175},
    {lineName: "L3", x: 810, y: 105},
    {lineName: "L4", x: 810, y: 25},
    //pkg
    {lineName: "L6", x: 90, y: 235},
    {lineName: "L7", x: 90, y: 150},
    {lineName: "L8", x: 285, y: 175},
    {lineName: "L9", x: 285, y: 290},
    {lineName: "CELL", x: 980, y: 185}
];
var testGroup = [
    {people: 4, x: 315, y: 80}, // group 39-42
    {people: 4, x: 315, y: 130}, // group 35-38
    {people: 5, x: 350, y: 200}, // group 30-34
    {people: 5, x: 350, y: 280} // group 25-29
];
var babGroup = [
    {people: 8, x: 530, y: 270, lineName: "L1"}, 
    {people: 8, x: 530, y: 190, lineName: "L2"}, 
    {people: 8, x: 530, y: 130, lineName: "L3"}, 
    {people: 8, x: 530, y: 50, lineName: "L4"}, 
    {people: 3, x: 0, y: 200, lineName: "L6"}, 
    {people: 3, x: 8, y: 115, lineName: "L7"}, 
    {people: 4, x: 145, y: 200, lineName: "L8"}, 
    {people: 4, x: 135, y: 290, lineName: "L9"} 
];

var maxTestTableNo = 42;
var sitefloor = 6;

