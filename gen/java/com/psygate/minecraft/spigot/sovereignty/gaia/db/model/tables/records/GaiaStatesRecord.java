/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.gaia.db.model.tables.records;


import com.psygate.minecraft.spigot.sovereignty.gaia.db.model.tables.GaiaStates;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.PlantState;

import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record11;
import org.jooq.Row11;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GaiaStatesRecord extends UpdatableRecordImpl<GaiaStatesRecord> implements Record11<Long, Integer, Integer, Integer, UUID, PlantState, Timestamp, Timestamp, UUID, String, Integer> {

	private static final long serialVersionUID = 1847563912;

	/**
	 * Setter for <code>nucleus.gaia_states.id</code>.
	 */
	public void setId(Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>nucleus.gaia_states.id</code>.
	 */
	public Long getId() {
		return (Long) getValue(0);
	}

	/**
	 * Setter for <code>nucleus.gaia_states.x</code>.
	 */
	public void setX(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>nucleus.gaia_states.x</code>.
	 */
	public Integer getX() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>nucleus.gaia_states.y</code>.
	 */
	public void setY(Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>nucleus.gaia_states.y</code>.
	 */
	public Integer getY() {
		return (Integer) getValue(2);
	}

	/**
	 * Setter for <code>nucleus.gaia_states.z</code>.
	 */
	public void setZ(Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>nucleus.gaia_states.z</code>.
	 */
	public Integer getZ() {
		return (Integer) getValue(3);
	}

	/**
	 * Setter for <code>nucleus.gaia_states.world_uuid</code>.
	 */
	public void setWorldUuid(UUID value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>nucleus.gaia_states.world_uuid</code>.
	 */
	public UUID getWorldUuid() {
		return (UUID) getValue(4);
	}

	/**
	 * Setter for <code>nucleus.gaia_states.plant_state</code>.
	 */
	public void setPlantState(PlantState value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>nucleus.gaia_states.plant_state</code>.
	 */
	public PlantState getPlantState() {
		return (PlantState) getValue(5);
	}

	/**
	 * Setter for <code>nucleus.gaia_states.transition_time</code>.
	 */
	public void setTransitionTime(Timestamp value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>nucleus.gaia_states.transition_time</code>.
	 */
	public Timestamp getTransitionTime() {
		return (Timestamp) getValue(6);
	}

	/**
	 * Setter for <code>nucleus.gaia_states.planted_time</code>.
	 */
	public void setPlantedTime(Timestamp value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>nucleus.gaia_states.planted_time</code>.
	 */
	public Timestamp getPlantedTime() {
		return (Timestamp) getValue(7);
	}

	/**
	 * Setter for <code>nucleus.gaia_states.creator</code>.
	 */
	public void setCreator(UUID value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>nucleus.gaia_states.creator</code>.
	 */
	public UUID getCreator() {
		return (UUID) getValue(8);
	}

	/**
	 * Setter for <code>nucleus.gaia_states.plant</code>.
	 */
	public void setPlant(String value) {
		setValue(9, value);
	}

	/**
	 * Getter for <code>nucleus.gaia_states.plant</code>.
	 */
	public String getPlant() {
		return (String) getValue(9);
	}

	/**
	 * Setter for <code>nucleus.gaia_states.growth_count</code>.
	 */
	public void setGrowthCount(Integer value) {
		setValue(10, value);
	}

	/**
	 * Getter for <code>nucleus.gaia_states.growth_count</code>.
	 */
	public Integer getGrowthCount() {
		return (Integer) getValue(10);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Long> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record11 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row11<Long, Integer, Integer, Integer, UUID, PlantState, Timestamp, Timestamp, UUID, String, Integer> fieldsRow() {
		return (Row11) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row11<Long, Integer, Integer, Integer, UUID, PlantState, Timestamp, Timestamp, UUID, String, Integer> valuesRow() {
		return (Row11) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field1() {
		return GaiaStates.GAIA_STATES.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return GaiaStates.GAIA_STATES.X;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field3() {
		return GaiaStates.GAIA_STATES.Y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field4() {
		return GaiaStates.GAIA_STATES.Z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<UUID> field5() {
		return GaiaStates.GAIA_STATES.WORLD_UUID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<PlantState> field6() {
		return GaiaStates.GAIA_STATES.PLANT_STATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Timestamp> field7() {
		return GaiaStates.GAIA_STATES.TRANSITION_TIME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Timestamp> field8() {
		return GaiaStates.GAIA_STATES.PLANTED_TIME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<UUID> field9() {
		return GaiaStates.GAIA_STATES.CREATOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field10() {
		return GaiaStates.GAIA_STATES.PLANT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field11() {
		return GaiaStates.GAIA_STATES.GROWTH_COUNT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value2() {
		return getX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value3() {
		return getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value4() {
		return getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID value5() {
		return getWorldUuid();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlantState value6() {
		return getPlantState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp value7() {
		return getTransitionTime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp value8() {
		return getPlantedTime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID value9() {
		return getCreator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value10() {
		return getPlant();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value11() {
		return getGrowthCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GaiaStatesRecord value1(Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GaiaStatesRecord value2(Integer value) {
		setX(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GaiaStatesRecord value3(Integer value) {
		setY(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GaiaStatesRecord value4(Integer value) {
		setZ(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GaiaStatesRecord value5(UUID value) {
		setWorldUuid(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GaiaStatesRecord value6(PlantState value) {
		setPlantState(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GaiaStatesRecord value7(Timestamp value) {
		setTransitionTime(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GaiaStatesRecord value8(Timestamp value) {
		setPlantedTime(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GaiaStatesRecord value9(UUID value) {
		setCreator(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GaiaStatesRecord value10(String value) {
		setPlant(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GaiaStatesRecord value11(Integer value) {
		setGrowthCount(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GaiaStatesRecord values(Long value1, Integer value2, Integer value3, Integer value4, UUID value5, PlantState value6, Timestamp value7, Timestamp value8, UUID value9, String value10, Integer value11) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		value6(value6);
		value7(value7);
		value8(value8);
		value9(value9);
		value10(value10);
		value11(value11);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached GaiaStatesRecord
	 */
	public GaiaStatesRecord() {
		super(GaiaStates.GAIA_STATES);
	}

	/**
	 * Create a detached, initialised GaiaStatesRecord
	 */
	public GaiaStatesRecord(Long id, Integer x, Integer y, Integer z, UUID worldUuid, PlantState plantState, Timestamp transitionTime, Timestamp plantedTime, UUID creator, String plant, Integer growthCount) {
		super(GaiaStates.GAIA_STATES);

		setValue(0, id);
		setValue(1, x);
		setValue(2, y);
		setValue(3, z);
		setValue(4, worldUuid);
		setValue(5, plantState);
		setValue(6, transitionTime);
		setValue(7, plantedTime);
		setValue(8, creator);
		setValue(9, plant);
		setValue(10, growthCount);
	}
}