package app.oengus.entity.model;

import app.oengus.spring.model.Views;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "schedule")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Schedule {

	@Id
	@JsonView(Views.Public.class)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "marathon_id")
	@JsonBackReference
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonView(Views.Public.class)
	private Marathon marathon;

	@OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("position ASC")
	@JsonView(Views.Public.class)
	private List<ScheduleLine> lines;

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Marathon getMarathon() {
		return this.marathon;
	}

	public void setMarathon(final Marathon marathon) {
		this.marathon = marathon;
	}

	public List<ScheduleLine> getLines() {
		return this.lines;
	}

	public void setLines(final List<ScheduleLine> lines) {
		this.lines = lines;
	}
}
