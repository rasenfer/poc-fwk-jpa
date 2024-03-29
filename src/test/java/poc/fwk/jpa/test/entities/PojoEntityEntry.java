package poc.fwk.jpa.test.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class PojoEntityEntry {

	@Id
	@GeneratedValue
	private Integer id;

	@Column
	private String value;
}
