package com.aehooo.tdsqn.entity.unit;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.aehooo.tdsqn.entity.GameEntity;
import com.aehooo.tdsqn.entity.ICasterEntity;
import com.aehooo.tdsqn.entity.ILiveEntity;
import com.aehooo.tdsqn.entity.ITouchHandler;
import com.aehooo.tdsqn.entity.ThisGameAnimatedSprite;
import com.aehooo.tdsqn.resources.ImageAlligator3000;
import com.aehooo.tdsqn.resources.TextureName;
import com.aehooo.tdsqn.utils.Constants;
import com.aehooo.tdsqn.utils.Vector2D;

public abstract class BasicUnit extends GameEntity implements ILiveEntity,
		ICasterEntity {
	private int hp;
	private int maxHp;
	private int mp;
	private int maxMp;
	private double aps;
	private int range;
	private double cd;

	public BasicUnit(final Vector2D pos) {
		super(pos);

		this.setSprite(new ThisGameAnimatedSprite(pos.getX(), pos.getY(), this
				.getMyTextureRegion()));

		this.setTouchHandler(new ITouchHandler() {

			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				return BasicUnit.this.onAreaTouched(pSceneTouchEvent,
						pTouchAreaLocalX, pTouchAreaLocalY);
			}
		});

		this.initializeConstants();
	}

	public BasicUnit(final float pX, final float pY) {
		this(new Vector2D(pX, pY));
	}

	private ITiledTextureRegion getMyTextureRegion() {
		ITiledTextureRegion textureRegion = ImageAlligator3000
				.getTiledTexture(TextureName.ZOMBIE);
		return textureRegion;
	}

	private void initializeConstants() {
		this.hp = 10;
		this.maxHp = this.hp;
		this.mp = 10;
		this.maxMp = this.mp;
		this.aps = 2;
		this.range = 0;
		this.cd = 0;
	}

	@Override
	public int getHP() {
		return this.hp;
	}

	@Override
	public double getPorcentagemHP() {
		if (this.maxHp == 0) {
			return 0;
		}

		return ((double) this.hp) / (this.maxHp);
	}

	@Override
	public boolean takeDamage(final int amount) {
		return false;
	}

	@Override
	public boolean receiveHeal(final int amount) {
		return false;
	}

	/*
	 * Parte referente a ICasterEntity
	 */

	@Override
	public int getMP() {
		return this.mp;
	}

	@Override
	public double getPorcentagemMP() {
		if (this.maxMp == 0) {
			return 0;
		}
		return ((double) this.mp) / (this.maxMp);
	}

	@Override
	public boolean spendMP(final int amount) {
		return false;
	}

	@Override
	public boolean receiveMP(final int amount) {
		return false;
	}

	@Override
	public double getAPS() {
		return this.aps;
	}

	@Override
	public void cooldown() {
		this.cd += (Constants.fps / this.aps);
	}

	@Override
	public int getRange() {
		return this.range;
	}

	public boolean isOnCooldown() {
		return this.cd > 0;
	}

	/*
	 * Meu
	 */

	public void animateLinha(final int linha) {
		int colunas = 3;
		long[] duracoes = new long[colunas];
		for (int i = 0; i < colunas; i++) {
			duracoes[i] = 100;
		}
		int[] frames = new int[colunas];
		for (int i = 0; i < colunas; i++) {
			frames[i] = (linha * colunas) + i;
		}
		this.getSprite().animate(duracoes, frames);
	}

	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		return false;
	}
}
