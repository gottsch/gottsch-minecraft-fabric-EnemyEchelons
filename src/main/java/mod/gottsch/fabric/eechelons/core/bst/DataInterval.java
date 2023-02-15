/*
 * This file is part of  Enemy Echelons.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Enemy Echelons is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Enemy Echelons is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Enemy Echelons.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.fabric.eechelons.core.bst;

import mod.gottsch.fabric.eechelons.EEchelons;
import mod.gottsch.fabric.gottschcore.nbt.INbtSerializer;
import net.minecraft.nbt.NbtCompound;

import java.util.function.Supplier;

/**
 * @author Mark Gottschling on Jul 25, 2022
 *
 * @param <D>
 */
public class DataInterval<D extends INbtSerializer> implements Comparable<DataInterval<D>> {
	private static final String LEFT_KEY = "left";
	private static final String RIGHT_KEY = "right";
	private static final String MIN_KEY = "min";
	private static final String MAX_KEY = "max";
	private static final String DATA_KEY = "data";
	
	public static final DataInterval<?> EMPTY = new DataInterval<>(-255, 320, null);
	
	private Integer start;
	private Integer end;
	private Integer min;
	private Integer max;
	private DataInterval<D> left;
	private DataInterval<D> right;

	// extra mod specific data
	private D data;
	
	private Supplier<D> dataSupplier;

	/**
	 * Empty constructor
	 */
	public DataInterval() {
		start = EMPTY.getStart();
		end = EMPTY.getEnd();
	}
	
	/**
	 * 
	 * @param supplier
	 */
	public DataInterval(Supplier<D> supplier) {
		this();
		this.dataSupplier = supplier;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 */
	public DataInterval(Integer start, Integer end, Supplier<D> supplier) {
		this.start = start;
		this.end = end;
		this.min = start < end ? start : end;
		this.max = start > end ? start : end;
		this.dataSupplier = supplier;
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @param data
	 * @param supplier
	 */
	public DataInterval(Integer start, Integer end, D data, Supplier<D> supplier) {
		this(start, end, supplier);
		this.data = data;
	}

	
	@Override
	public int compareTo(DataInterval<D> interval) {
		if (getStart() < interval.getStart()) {
			return -1;
		} else if (getStart() == interval.getStart()) {
			if (getEnd() == interval.getEnd()) {
				return 0;
			}
			EEchelons.LOGGER.debug("this.end -> {}, interval.end -> {}", this.getEnd(), interval.getEnd());
			return this.getEnd() < interval.getEnd() ? -1 : 1;
		} else {
			EEchelons.LOGGER.debug("this.end -> {}, interval.end -> {}", this.getEnd(), interval.getEnd());
			return 1;
		}
	}

	/**
	 * 
	 * @param nbt
	 */
	public void save(NbtCompound nbt) {
		EEchelons.LOGGER.debug("saving interval -> {}", this);
		
		nbt.putInt("start", start);
		nbt.putInt("end", end);

		nbt.putInt(MIN_KEY, min);
		nbt.putInt(MAX_KEY, max);
		
//		NbtCompound dataNbt = new NbtCompound();
		if (getData() != null) {
			NbtCompound dataNbt = getData().writeNbt();
			nbt.put(DATA_KEY, dataNbt);
		}
		
		if (getLeft() != null) {
			NbtCompound left = new NbtCompound();
			getLeft().save(left);
			nbt.put(LEFT_KEY, left);
		}

		if (getRight() != null) {
			NbtCompound right = new NbtCompound();
			getRight().save(right);
			nbt.put(RIGHT_KEY, right);
		}
	}

	/**
	 * 
	 * @param nbt
	 * @return
	 */
	public void load(NbtCompound nbt) {
		if (nbt == null) {
			return;
		}

		int start = EMPTY.getStart();
		int end = EMPTY.getEnd();
		
		if (nbt.contains("start")) {
			start = nbt.getInt("start");
		}
		else {
			this.start = start;
		}
		if (nbt.contains("end")) {
			end = nbt.getInt("end");
		}
		else {
			this.end = end;
		}

		if (nbt.contains(MIN_KEY)) {
			setMin(nbt.getInt(MIN_KEY));
		}
		if (nbt.contains(MAX_KEY)) {
			setMax(nbt.getInt(MAX_KEY));
		}
		
		if (nbt.contains(DATA_KEY) && dataSupplier != null) {
			NbtCompound dataNbt = (NbtCompound) nbt.get(DATA_KEY);
			D data = dataSupplier.get();
			data.readNbt(dataNbt);
			setData(data);
		}
		
		if (nbt.contains(LEFT_KEY)) {
			DataInterval<D> left = new DataInterval<>(dataSupplier);
			left.load((NbtCompound) nbt.get(LEFT_KEY));
			if (!left.equals(DataInterval.EMPTY)) {
				setLeft(left);
			}
		}
		
		if (nbt.contains(RIGHT_KEY)) {
			DataInterval<D> right = new DataInterval<>(dataSupplier);
			right.load((NbtCompound) nbt.get(RIGHT_KEY));
			if (!right.equals(DataInterval.EMPTY)) {
				setRight(right);
			}			
		}		
		EEchelons.LOGGER.debug("loaded -> {}", this);
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}
	
	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public DataInterval<D> getLeft() {
		return left;
	}

	public void setLeft(DataInterval<D> left) {
		this.left = left;
	}

	public DataInterval<D> getRight() {
		return right;
	}

	public void setRight(DataInterval<D> right) {
		this.right = right;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataInterval<?> other = (DataInterval<?>) obj;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (end == null) {
			return other.end == null;
		} else {
			return end.equals(other.end);
		}
	}

	public D getData() {
		return data;
	}

	public void setData(D data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Interval [start=" + start + ", end=" + end + ", min=" + min + ", max=" + max + ", left=" + left
				+ ", right=" + right + ", data=" + data + "]";
	}
	
}
