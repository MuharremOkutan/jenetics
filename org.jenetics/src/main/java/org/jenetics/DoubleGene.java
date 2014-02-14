/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jenetics;

import static org.jenetics.util.math.random.nextDouble;

import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.jenetics.internal.util.model.ModelType;
import org.jenetics.internal.util.model.ValueType;

import org.jenetics.util.Array;
import org.jenetics.util.ISeq;
import org.jenetics.util.Mean;
import org.jenetics.util.RandomRegistry;

/**
 * Implementation of the NumericGene which holds a 64 bit floating point number.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version @__version__@ &mdash; <em>$Date: 2014-02-14 $</em>
 * @since @__version__@
 */
@XmlJavaTypeAdapter(DoubleGene.Model.Adapter.class)
public final class DoubleGene
	extends NumericGene<Double, DoubleGene>
	implements Mean<DoubleGene>
{

	private static final long serialVersionUID = 1L;

	/**
	 * Create a new random {@code DoubleGene} with the given value and the
	 * given range. If the {@code value} isn't within the interval [min, max),
	 * no exception is thrown. In this case the method
	 * {@link DoubleGene#isValid()} returns {@code false}.
	 *
	 * @param value the value of the gene.
	 * @param min the minimal valid value of this gene (inclusively).
	 * @param max the maximal valid value of this gene (exclusively).
	 * @throws NullPointerException if one of the arguments is {@code null}.
	 */
	public DoubleGene(final Double value, final Double min, final Double max) {
		super(value, min, max);
	}

	/**
	 * Create a new random {@code DoubleGene} with the given value and the
	 * given range. If the {@code value} isn't within the interval [min, max),
	 * no exception is thrown. In this case the method
	 * {@link DoubleGene#isValid()} returns {@code false}.
	 *
	 * @param value the value of the gene.
	 * @param min the minimal valid value of this gene (inclusively).
	 * @param max the maximal valid value of this gene (exclusively).
	 */
	public static DoubleGene of(
		final double value,
		final double min,
		final double max
	) {
		return new DoubleGene(value, min, max);
	}

	/**
	 * Create a new random {@code DoubleGene}. It is guaranteed that the value
	 * of the {@code DoubleGene} lies in the interval [min, max).
	 *
	 * @param min the minimal valid value of this gene (inclusively).
	 * @param max the maximal valid value of this gene (exclusively).
	 */
	public static DoubleGene of(final double min, final double max) {
		return of(nextDouble(RandomRegistry.getRandom(), min, max), min, max);
	}

	static ISeq<DoubleGene> seq(
		final Double minimum,
		final Double maximum,
		final int length
	) {
		final double min = minimum;
		final double max = maximum;
		final Random r = RandomRegistry.getRandom();

		final Array<DoubleGene> genes = new Array<>(length);
		for (int i = 0; i < length; ++i) {
			genes.set(i, new DoubleGene(nextDouble(r, min, max), minimum, maximum));
		}
		return genes.toISeq();
	}

	@Override
	public DoubleGene newInstance(final Number number) {
		return new DoubleGene(number.doubleValue(), _min, _max);
	}

	@Override
	public DoubleGene newInstance() {
		return new DoubleGene(
			nextDouble(RandomRegistry.getRandom(), _min, _max), _min, _max
		);
	}

	@Override
	public DoubleGene mean(final DoubleGene that) {
		return new DoubleGene(_value + (that._value - _value) / 2.0, _min, _max);
	}

	/* *************************************************************************
	 *  JAXB object serialization
	 * ************************************************************************/

	@XmlRootElement(name = "org.jenetics.DoubleGene")
	@XmlType(name = "org.jenetics.DoubleGene")
	@XmlAccessorType(XmlAccessType.FIELD)
	final static class Model {

		@XmlAttribute
		public double min;

		@XmlAttribute
		public double max;

		@XmlValue
		public double value;

		@ValueType(DoubleGene.class)
		@ModelType(Model.class)
		public final static class Adapter
			extends XmlAdapter<Model, DoubleGene>
		{
			@Override
			public Model marshal(final DoubleGene value) {
				final Model m = new Model();
				m.min = value.getMin();
				m.max = value.getMax();
				m.value = value.getAllele();
				return m;
			}

			@Override
			public DoubleGene unmarshal(final Model m) {
				return DoubleGene.of(m.value, m.min, m.max);
			}
		}
	}

}