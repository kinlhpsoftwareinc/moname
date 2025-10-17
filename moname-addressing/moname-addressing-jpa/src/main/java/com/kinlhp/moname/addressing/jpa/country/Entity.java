package com.kinlhp.moname.addressing.jpa.country;

import java.io.Serial;
import java.io.Serializable;

import jakarta.annotation.Nonnull;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.converter.StringArrayConverter;
import com.kinlhp.moname.commons.jpa.entity.AbstractReadOnly;

/**
 * Country entity.
 */
@AttributeOverride(column = @Column(name = "alpha_2_code"), name = "alpha2Code")
@AttributeOverride(column = @Column(name = "alpha_3_code"), name = "alpha3Code")
@AttributeOverride(column = @Column(name = "numeric_code"), name = "pk")
@jakarta.persistence.Entity
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "country")
@ToString(onlyExplicitlyIncluded = true)
public class Entity extends AbstractReadOnly<Integer> {

	@Serial
	private static final long serialVersionUID = -7578939088238956730L;

	/**
	 * ISO 3166-1 alpha-2 codes are two-letter country codes defined in ISO 3166-1.
	 */
	@Column(name = "alpha_2_code")
	@Getter
	@Nonnull
	@NotNull
	@Setter
	@Size(max = 2, min = 2)
	@ToString.Include
	private String alpha2Code;

	/**
	 * ISO 3166-1 alpha-3 codes are three-letter country codes defined in ISO 3166-1.
	 */
	@Column(name = "alpha_3_code")
	@Getter
	@Nonnull
	@NotNull
	@Setter
	@Size(max = 3, min = 3)
	@ToString.Include
	private String alpha3Code;

	/**
	 * The term country refers to a political state or nation or its territory.
	 */
	@Column(name = "english_name")
	@Getter
	@Nonnull
	@NotNull
	@Setter
	@Size(max = 58, min = 4)
	@ToString.Include
	private String englishName;

	/**
	 * Le terme pays fait référence à un État politique ou à une nation ou à son territoire.
	 */
	@Column(name = "french_name")
	@Getter
	@Nonnull
	@NotNull
	@Setter
	@Size(max = 56, min = 4)
	@ToString.Include
	private String frenchName;

	/**
	 * Country code top-level domain (ccTLD).
	 */
	@Column(name = "internet_cctld")
	@Convert(converter = StringArrayConverter.class)
	@Getter/*(onMethod_ = @Nullable)*/
	@Nonnull
	@Setter
	@Size(max = 2)
	@ToString.Include
	private String[] internetCctld;

	/**
	 * O termo país refere-se a um estado político ou nação ou seu território.
	 */
	@Column(name = "portuguese_name")
	@Getter
	@Nonnull
	@NotNull
	@Setter
	@Size(max = 46, min = 3)
	@ToString.Include
	private String portugueseName;

	@Id
	@Max(894L)
	@Min(4L)
	@Nonnull
	@NotNull
	@Override
	@ToString.Include(name = "numericCode", rank = 1)
	public Integer getPk() {
		return primaryKey();
	}

	/**
	 * ISO 3166-1 numeric codes are three-digit (left padded with zero) country codes defined in ISO 3166-1.
	 *
	 * @see AbstractReadOnly#getPk() getPk
	 */
	@Nonnull
	@Transient
	public String getNumericCode() {
		return com.kinlhp.moname.addressing.domain.country.Entity.numericCodeOf(getPk());
	}

	/**
	 * @param numericCodeLeadingZero the country numeric code.
	 * @see AbstractReadOnly#setPk(Serializable) setPk
	 */
	public void setNumericCode(@Size(max = 3, min = 3) String numericCodeLeadingZero) {
		setPk(com.kinlhp.moname.addressing.domain.country.Entity.numericCodeOf(numericCodeLeadingZero));
	}
}
