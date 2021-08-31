import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class RouteCalculatorTest extends TestCase
{
    private RouteCalculator routeCalculator;
    private StationIndex stationIndex;
    private Line line1 = new Line(1, "green");
    private Line line2 = new Line(2, "blue");
    private Line line3 = new Line(3, "pink");
    private Station green1 = new Station("green1", line1);
    private Station green2 = new Station("green2", line1);
    private Station green3 = new Station("green3", line1);
    private Station green4 = new Station("green4", line1);
    private Station blue1 = new Station("blue1", line2);
    private Station blue2 = new Station("blue2", line2);
    private Station blue3 = new Station("blue3", line2);
    private Station blue4 = new Station("blue4", line2);
    private Station pink1 = new Station("pink1", line3);
    private Station pink2 = new Station("pink2", line3);
    private Station pink3 = new Station("pink3", line3);
    private Station pink4 = new Station("pink4", line3);

    @Override
    protected void setUp() throws Exception
    {
        line1.addStation(green1);
        line1.addStation(green2);
        line1.addStation(green3);
        line1.addStation(green4);
        line2.addStation(blue1);
        line2.addStation(blue2);
        line2.addStation(blue3);
        line2.addStation(blue4);
        line3.addStation(pink1);
        line3.addStation(pink2);
        line3.addStation(pink3);
        line3.addStation(pink4);
        stationIndex = new StationIndex();
        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);
        line1.getStations().forEach(stationIndex :: addStation);
        line2.getStations().forEach(stationIndex :: addStation);
        line3.getStations().forEach(stationIndex :: addStation);
        stationIndex.addConnection(new ArrayList<>(List.of(green3, blue2)));
        stationIndex.addConnection(new ArrayList<>(List.of(blue3, pink2)));
        routeCalculator = new RouteCalculator(stationIndex);
    }

    public void testGetShortestRouteNoConnections(){
        List<Station> expected = new ArrayList<>(List.of(green1, green2, green3, green4));
        List<Station> actual = routeCalculator.getShortestRoute(green1, green4);
        assertEquals(expected, actual);
    }

    public void testCalculateDurationShortRouteNoInterchange(){
        List<Station> route = new ArrayList<>(List.of(green1, green2, green3, green4));
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 7.5;
        assertEquals(expected, actual);
    }

    public void testGetShortestRouteWithOneConnections(){
        List<Station> actual = routeCalculator.getShortestRoute(green1, blue1);
        List<Station> expected = new ArrayList<>(List.of(green1, green2, green3, blue2, blue1));
        assertEquals(expected, actual);
    }

    public void testCalculateDurationShortRouteWithOneInterchange(){
        List<Station> route = new ArrayList<>(List.of(green1, green2, green3, blue2, blue1));
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 11.0;
        assertEquals(expected, actual);
    }

    public void testGetShortestRouteWithTwoConnections() {
        List<Station> actual = routeCalculator.getShortestRoute(green2, pink3);
        List<Station> expected = new ArrayList<>(List.of(green2, green3, blue2, blue3, pink2, pink3));
        assertEquals(expected, actual);
    }

    public void testCalculateDurationShortRouteWithTwoInterchange(){
        List<Station> route = new ArrayList<>(List.of(green2, green3, blue2, blue3, pink2, pink3));
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 14.5;
        assertEquals(expected, actual);
    }
}
