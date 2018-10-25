package com.example.demo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.demo.flights.Flight;
import com.example.demo.flights.FlightRepository;
import com.example.demo.flights.airport.Airport;
import com.example.demo.flights.airport.AirportRepository;
import com.example.demo.flights.route.FlightRoute;
import com.example.demo.flights.route.FlightRouteRepository;
import com.example.demo.flights.route.FlightRouteTime;
import com.example.demo.flights.route.FlightRouteTimeRepository;
import com.example.demo.security.auth.RoleRepository;
import com.example.demo.security.auth.UserRole;
import com.example.demo.security.auth.UserRoleName;
import com.example.demo.users.User;
import com.example.demo.users.UserRepository;
import com.example.demo.users.login.UserLogin;
import com.example.demo.users.login.UserLoginRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TestDataSetup {

    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private FlightRouteRepository flightRouteRepository;

    @Autowired
    FlightRepository m_flightRepository;
    @Autowired
    private FlightRouteTimeRepository flightRouteTimeRepository;


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;


    public void setUpFlightsCoreData() {
        setUpAirports();
        setUpRoutes();
        setUpFlights();
    }

    public void setUpAirports() {
        createAirport("BCN", "Barcelona International Airport");
        createAirport("CDG", "Charles de Gaulle Airport");
        createAirport("ATL", "Hartsfield–Jackson Atlanta International Airport");
        createAirport("PEK", "Beijing Capital International Airport");
        createAirport("SVO", "Sheremetyevo International Airport");
        createAirport("SOF", "Sofia International Airport");
        createAirport("EDI", "Edinburgh International Airport");
    }

    private void createAirport(String airportCode, String airportName) {
        Airport airport = new Airport();
        airport.setAirportCode(airportCode);
        airport.setAirportName(airportName);
        airportRepository.save(airport);
    }

    public void setUpRoutes() {
        FlightRoute fr = createFligthRoute("SOF", "EDI");
        List<FlightRouteTime> flightRouteTimes =
                createTimes(fr, DayOfWeek.MONDAY, LocalTime.of(12, 0), LocalTime.of(21, 30));
        flightRouteTimes.addAll(createTimes(fr, DayOfWeek.MONDAY, LocalTime.of(13, 30), LocalTime.of(18, 30)));
        fr.setFlightRouteTimes(flightRouteTimes);
        flightRouteRepository.save(fr);
        System.out.println(fr);
    }

    private FlightRoute createFligthRoute(String origCode, String destCode) {
        FlightRoute fr = new FlightRoute();
        fr.setOrigin(airportRepository.findByAirportCode(origCode).get());
        fr.setDestination(airportRepository.findByAirportCode(destCode).get());
        return flightRouteRepository.save(fr);
    }

    private List<FlightRouteTime> createTimes(FlightRoute fr, DayOfWeek dayOfWeek, LocalTime... times) {
        return Stream.of(times).map(t -> {
            FlightRouteTime frt = new FlightRouteTime();
            frt.setDayOfWeek(dayOfWeek);
            frt.setTime(t);
            frt.setFlightRoute(fr);
            return frt;
        }).map(frt -> flightRouteTimeRepository.save(frt)).collect(Collectors.toList());
    }

    private void setUpFlights() {
        flightRouteTimeRepository.findAll().stream().forEach(frt -> {
            Flight f = TestDataSetup.createFlightByFlightRouteTime(frt);
            m_flightRepository.save(f);
        });
    }


    private static LocalDate createDate(DayOfWeek dow) {
        LocalDate now = LocalDate.now();
        return now.with(TemporalAdjusters.next(dow));

    }

    private static Flight createFlightByFlightRouteTime(FlightRouteTime frt) {
        Flight flight = new Flight();
        flight.setFlightDate(createDate(frt.getDayOfWeek()));
        flight.setFlightRouteTime(frt);
        return flight;
    }



    public User setUpUser(){
        User user = new User();
        user.setName("John Doe");
        user.setEmail("test@test.com");
        user.setUserRoles(new HashSet<>(Lists.newArrayList(getUserRole())));
        UserLogin ul = new UserLogin();
        ul.setUser(user);
        ul.setPassword("password");
        ul.setUsername(user.getEmail());
        userRepository.save(user);
        userLoginRepository.save(ul);
        return user;
    }

    public UserRole getUserRole() {
        return roleRepository.findByName(UserRoleName.ROLE_USER)
            .orElse(roleRepository.save(new UserRole(UserRoleName.ROLE_USER)));
    }
}