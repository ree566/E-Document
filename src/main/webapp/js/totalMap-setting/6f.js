var pXa = -20;
var pYa = -20;

var titleGroup = [
    //assy
    {lineName: "L3", x: 1080, y: 280},
    {lineName: "L4", x: 1080, y: 180},
    //pkg
    {lineName: "L6", x: 190, y: 200},
    {lineName: "L7", x: 190, y: 105},
    {lineName: "L8", x: 525, y: 220},
    {lineName: "L9", x: 525, y: 280}
];
var testGroup = [
    {people: 4, x: 580, y: 80}, // group 15-18
    {people: 4, x: 580, y: 160}, // group 11-14
    {people: 5, x: 610, y: 210}, // group 6-10
    {people: 5, x: 610, y: 290} // group 1-5
];
var babGroup = [
    {people: 8, x: 790, y: 280, lineName: "L3"}, // group 1-4
    {people: 8, x: 790, y: 190, lineName: "L4"}, // group 21-24
    {people: 3, x: 255, y: 210, lineName: "L6"}, // group 16-20
    {people: 3, x: 255, y: 120, lineName: "L7"}, // group 9-15
    {people: 4, x: 400, y: 190, lineName: "L8"}, // group 5-8
    {people: 4, x: 400, y: 320, lineName: "L9"} // group 1-4
];

var maxTestTableNo = 42;