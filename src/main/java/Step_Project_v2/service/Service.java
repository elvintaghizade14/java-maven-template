package Step_Project_v2.service;

import Step_Project_v2.dao.DAOBookingFileText;
import Step_Project_v2.dao.DAOFlightFileText;
import Step_Project_v2.entity.Booking;
import Step_Project_v2.entity.Flight;
import Step_Project_v2.entity.Passenger;
import Step_Project_v2.helpers.FlightGenerator;
import Step_Project_v2.helpers.Predicates;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Service {
  DAOBookingFileText daoBooking;
  DAOFlightFileText daoFlight;

  public Service(DAOBookingFileText daoBooking, DAOFlightFileText daoFlight) {
    this.daoBooking = daoBooking;
    this.daoFlight = daoFlight;
  }

  public List<String> getAllFlights() {
    return daoFlight.getAllBy(Predicates.isSomeHoursBefore(24))
            .stream().map(Flight::represent).collect(Collectors.toList());
  }

  public String getFlightById(int flightId) {
    return daoFlight.get(flightId).map(Flight::represent)
            .orElse("No flight found");
  }

  public List<String> searchForBook(String dest, LocalDate date, int numOfPeople) {
    return daoFlight.getAllBy(Predicates.isBookable(dest, date, numOfPeople))
            .stream().map(Flight::represent).collect(Collectors.toList());
  }

  public void book(int flightId, List<Passenger> passengers) {
    daoBooking.create(new Booking(flightId, passengers));
    Optional<Flight> flightExtra = daoFlight.get(flightId);
    daoFlight.delete(flightId);
    flightExtra.ifPresent(f -> f.setFreeSpaces(f.getFreeSpaces() - passengers.size()));
    daoFlight.create(flightExtra.orElseThrow(RuntimeException::new));
  }

  public String cancelBooking(int bookingId) {
    Optional<Booking> booking = daoBooking.get(bookingId);
    if (booking.isPresent()) {
      Booking b = daoBooking.get(bookingId).get();
      Optional<Flight> flightExtra = daoFlight.get(b.getFlight_id());
      daoFlight.delete(b.getFlight_id());
      daoBooking.delete(bookingId);
      flightExtra.ifPresent(f -> f.setFreeSpaces(f.getFreeSpaces() + b.getPassengers().size()));
      daoFlight.create(flightExtra.orElseThrow(RuntimeException::new));
      return "Booking deleted.";
    } else return "There is no any booking.";
  }

  public List<String> getMyFlights(String name, String surname) {
    return daoBooking.getAllBy(Predicates.isMyFlight(name, surname))
            .stream().map(Booking::represent).collect(Collectors.toList());
  }

  public boolean getAll() {
    return daoFlight.getAll().size() == 0;
  }

  public void addFlight() {
    daoFlight.create(FlightGenerator.genFlight());
  }

  public String showMenu() {
    StringBuilder sb = new StringBuilder();
    return sb.append("1. Online - board\n")
            .append("2. Show the flight info\n")
            .append("3. Search and book a flight\n")
            .append("4. Cancel the booking\n")
            .append("5. My flights\n")
            .append("6. Exit")
            .toString();
  }
}