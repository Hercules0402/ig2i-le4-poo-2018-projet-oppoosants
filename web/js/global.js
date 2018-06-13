var isGraphic = false;
var drawed = false;
var isCbmDisplayed = false;
var displayLocations = false;
var displayLines = false;
var displayPoint = false;
var pointSize = 7;
var idCurrentInstance, tabSolution, allLocations, coeffW, coeffH;

//Point mouvant
var xP, yP, lastStopX, lastStopY;
var movingPState = "end";
var lastVisitedP = 0;
var pointsOrder = [];
var lastCircle;
var movingHistoric = [];
var historicSize = 10;
var speed = 100;