package http.program.dto.bnm;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Valute")
public class Valute {

    @XStreamAlias("ID")
    @XStreamAsAttribute
    private Integer Id;

    @XStreamAlias("NumCode")
    private String NumCode;

    @XStreamAlias("CharCode")
    private String CharCode;

    @XStreamAlias("Nominal")
    private Integer Nominal;

    @XStreamAlias("Name")
    private String Name;

    @XStreamAlias("Value")
    private Double Value;

    public Double getValue() {
        return Value;
    }

    public Integer getId() {
        return Id;
    }

    public Integer getNominal() {
        return Nominal;
    }

    public String getNumCode() {
        return NumCode;
    }

    public String getCharCode() {
        return CharCode;
    }

    public String getName() {
        return Name;
    }

    public void setCharCode(String charCode) {
        CharCode = charCode;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setNominal(Integer nominal) {
        Nominal = nominal;
    }

    public void setNumCode(String numCode) {
        NumCode = numCode;
    }

    public void setValue(Double value) {
        Value = value;
    }
}
