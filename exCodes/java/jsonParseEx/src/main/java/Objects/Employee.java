package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {
    private String name;
    private POSITION position;
//    private List<String> skilltree;
    private Address address;

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", position=" + position +
//                ", skilltree=" + skilltree +
                ", address=" + address +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public POSITION getPosition() {
        return position;
    }

    public void setPosition(POSITION position) {
        this.position = position;
    }

//    public List<String> getSkilltree() {
//        return skilltree;
//    }
//
//    public void setSkilltree(List<String> skilltree) {
//        this.skilltree = skilltree;
//    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
