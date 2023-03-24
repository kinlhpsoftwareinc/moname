package com.kinlhp.moname.addressing.domain.country;

import java.io.Serial;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.AbstractReadOnly;

/**
 * Country entity.
 */
@AttributeOverride(column = @Column(name = "numeric_code"), name = "pk")
@jakarta.persistence.Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "country")
public class Entity extends AbstractReadOnly<Integer> {

	@Serial
	private static final long serialVersionUID = 5099824014758215388L;

	/**
	 * ISO 3166-1 alpha-2 codes are two-letter country codes defined in ISO 3166-1.
	 */
	@Builder.Default
	@Column(name = "alpha_2_code")
	@Getter
	@NotBlank
	@Setter(onParam_ = {@NotNull})
	private String alpha2Code = "";

	/**
	 * ISO 3166-1 alpha-3 codes are three-letter country codes defined in ISO 3166-1.
	 */
	@Builder.Default
	@Column(name = "alpha_3_code")
	@Getter
	@NotBlank
	@Setter(onParam_ = {@NotNull})
	private String alpha3Code = "";

	/**
	 * The term country refers to a political state or nation or its territory.
	 */
	@Builder.Default
	@Column(name = "english_name")
	@Getter
	@NotBlank
	@Setter(onParam_ = {@NotNull})
	private String englishName = "";

	/**
	 * Le terme pays fait référence à un État politique ou à une nation ou à son territoire.
	 */
	@Builder.Default
	@Column(name = "french_name")
	@Getter
	@NotBlank
	@Setter(onParam_ = {@NotNull})
	private String frenchName = "";

	/**
	 * Country code top-level domain (ccTLD).
	 */
	@Builder.Default
	@Column(name = "internet_cctld")
	@Getter
	private String internetCctld = "";

	/**
	 * O termo país refere-se a um estado político ou nação ou seu território.
	 */
	@Builder.Default
	@Column(name = "portuguese_name")
	@Getter
	@NotBlank
	@Setter(onParam_ = {@NotNull})
	private String portugueseName = "";

}
