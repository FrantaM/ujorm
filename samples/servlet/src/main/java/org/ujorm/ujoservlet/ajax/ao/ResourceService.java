package org.ujorm.ujoservlet.ajax.ao;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.ujorm.tools.web.ao.WebUtils;

/**
 *
 * @author Pavel Ponec
 */
public class ResourceService {

    /** Logger */
    private static final Logger LOGGER = Logger.getLogger(ResourceService.class.toString());
    private static final String HOTELS_CSV = "/csv/ResourceHotel.csv";

    private List<Hotel> hotels = null;

    public Stream<Hotel> getHotels() throws IOException {
        if (hotels == null) {
            synchronized (this) {
                try (Stream<Hotel> hotelSteam =  loadHotels(getClass().getResource(HOTELS_CSV))) {
                    hotels = hotelSteam.collect(Collectors.toList());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Hotel reading fails", e);
                    return Stream.empty();
                }
            }
        }
        return hotels.stream();
    }

    /**
     * Direct stream of data source.
     * @return
     */
    public Stream<Hotel> loadHotelStream() throws IOException {
        return loadHotels(getClass().getResource(HOTELS_CSV));
    }

    /**
     * Return a raw stream
     *
     * ( Hotel.NAME
     * , Hotel.NOTE
     * , Hotel.CITY.add(City.ID) // The value is a foreign key!
     * , Hotel.STREET
     * , Hotel.PHONE
     * , Hotel.STARS
     * , Hotel.HOME_PAGE
     * , Hotel.PRICE
     * , Hotel.ACTIVE
     * @return
     */
    protected Stream<Hotel> loadHotels(URL url) throws IOException {

        //read file into stream, try-with-resources
        return WebUtils.rowsOfUrl(url)
                .filter(t -> !t.startsWith("* "))
                .filter(t -> !t.startsWith("NAME;"))
                .map(t -> {
                    Hotel hotel = null;
                    String[] c = t.split(";");
                    if (c.length > 8) {
                        hotel = new Hotel();
                        hotel.setName(c[0]);
                        hotel.setNote(c[1]);
                        hotel.setCity(c[2]);
                        hotel.setStreet(c[3]);
                        hotel.setPhone(c[4]);
                        hotel.setStars(Float.parseFloat(c[5]));
                        hotel.setHomePage(c[6]);
                        hotel.setPrice(new BigDecimal(c[7]));
                        hotel.setCurrency("USD");
                        hotel.setActive(Boolean.parseBoolean(c[8]));
                    }
                    return hotel;
                })
                .filter(t -> t != null);
    }

}