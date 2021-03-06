package com.aehooo.tdsqn.entity.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import com.aehooo.tdsqn.annotations.TextureInfo;
import com.aehooo.tdsqn.entity.ILiveEntity;
import com.aehooo.tdsqn.entity.ITargetEntity;
import com.aehooo.tdsqn.entity.IUpdatable;
import com.aehooo.tdsqn.entity.impl.AttrModifier;
import com.aehooo.tdsqn.entity.impl.GameEntity;
import com.aehooo.tdsqn.entity.unit.BasicUnit;
import com.aehooo.tdsqn.manager.LevelManager;
import com.aehooo.tdsqn.path.Path;
import com.aehooo.tdsqn.resources.TextureName;
import com.aehooo.tdsqn.utils.Vector2D;

@TextureInfo(name = TextureName.GROUP)
public class Group extends GameEntity implements IUpdatable, ITargetEntity {

	private Vector2D direction;
	private double vel;
	private Path path;
	private boolean walking;
	private boolean initialized;
	private int nextPointIdx;
	private Map<String, AttrModifier> modificadores;

	private List<BasicUnit> unidades;

	public Group(final Scene fScene, final Path path) throws Exception {
		super(fScene, path.getPoint(0));
		this.direction = path.getDir(0, 1);
		this.nextPointIdx = 1;
		this.vel = 2.0;
		this.path = path;
		this.walking = false;
		this.initialized = false;
		this.unidades = new ArrayList<BasicUnit>();
		this.modificadores = new HashMap<String, AttrModifier>();

	}

	public boolean isInitialized() {
		return this.initialized;
	}

	@Override
	public void animateLine(final int linha) {
		long[] duracoes = new long[4];
		for (int i = 0; i < 4; i++) {
			duracoes[i] = 400;
		}
		int[] frames = { 0, 1, 2, 1 };
		this.animate(duracoes, frames);
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if (pSceneTouchEvent.isActionDown()) {
			this.walking = !this.walking;
			this.initialized = true;
			return true;
		}
		return false;
	}

	@Override
	public void setPos(final Vector2D v) {
		Vector2D diff = v.sub(this.getPos());
		super.setPos(v);
		if (this.unidades != null) {
			for (BasicUnit u : this.unidades) {
				u.selectRotation(diff);
			}
		}
	}

	public boolean isAhead(final Group g) {
		Vector2D v = g.getPos().sub(this.getPos()).normalize();
		double dx = v.getX() * this.direction.getX();
		double dy = v.getY() * this.direction.getY();

		return (dx < 0) || (dy < 0);
	}

	public void walk(final double dRemain) {
		if (this.direction == null) {
			this.walking = false;
			return;
		}

		for (Group g : LevelManager.getCurrentLevelScene().getGroups()) {
			if (g.isDead()) {
				continue;
			}
			double dist = Vector2D.dist(this.getPos(), g.getPos());
			if ((this != g) && (dist <= 100) && g.isAhead(this)) {
				return;
			}
		}
		Vector2D newpos = this.getPos().add(this.direction.mul(dRemain));
		Vector2D nextPoint = this.path.getPoint(this.nextPointIdx);

		if (this.path.isBeyond(this.nextPointIdx, newpos)) {
			double dist = this.getPos().distTo(nextPoint);

			double remaining = dRemain - dist;

			this.setPos(nextPoint);
			this.nextPointIdx++;
			if (this.nextPointIdx >= this.path.size()) {
				this.direction = null;
				for (BasicUnit u : this.unidades) {
					u.setTerminouPercurso(true);
				}
				this.shouldDie();
				return;
			}
			this.direction = this.path.getDir(this.nextPointIdx - 1,
					this.nextPointIdx);

			this.walk(remaining);
		} else {
			this.setPos(newpos);
		}
	}

	public boolean addUnit(final BasicUnit u) {
		int n = this.unidades.size();
		if (this.initialized || (u == null) || (n >= 4)) {
			return false;
		}

		u.setPos(((n / 2) * 40), ((n % 2) * 40));

		this.unidades.add(u);

		this.getSprite().attachChild(u.getSprite());

		return true;
	}

	public List<BasicUnit> getUnits() {
		return this.unidades;
	}

	public double calculateVel() {
		double vel = this.vel;

		for (BasicUnit u : this.unidades) {
			if (u.getVel() < vel) {
				vel = u.getVel();
			}
		}

		AttrModifier attrModifier = this.modificadores.get("vel");
		if (attrModifier != null) {
			vel *= attrModifier.getPctgTotal();
		}

		return vel;
	}

	@Override
	public void shouldDie() {

		for (BasicUnit u : this.unidades) {
			u.shouldDie();
		}

		super.shouldDie();
	}

	@Override
	public void onFrameUpdate() {
		for (BasicUnit u : this.unidades) {
			u.onFrameUpdate();
		}
		for (AttrModifier mod : this.modificadores.values()) {
			mod.onFrameUpdate();
		}
		if (this.walking) {
			this.walk(this.calculateVel());
		}
	}

	@Override
	public void onCheckDeadChildren() {
		for (AttrModifier mod : this.modificadores.values()) {
			mod.onCheckDeadChildren();
		}
		List<BasicUnit> mortos = new ArrayList<BasicUnit>();
		for (BasicUnit u : this.unidades) {
			if (u.isDead()) {
				mortos.add(u);
			}
		}
		this.unidades.removeAll(mortos);
		if ((this.unidades.size() <= 0) && this.initialized) {
			this.shouldDie();
		}
	}

	@Override
	public boolean takeDamage(final int amount) {
		for (BasicUnit u : this.unidades) {
			u.takeDamage(amount);
		}
		return true;
	}

	@Override
	public boolean receiveHeal(final int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean slow(final ILiveEntity origin, final double pctg,
			final int framesDur) {
		AttrModifier attrModifier = this.modificadores.get("vel");
		if (attrModifier == null) {
			attrModifier = new AttrModifier();
			this.modificadores.put("vel", attrModifier);
		}
		attrModifier.put(origin, pctg, framesDur);
		return false;
	}
}
