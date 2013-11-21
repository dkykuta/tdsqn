package com.aehooo.tdsqn.entity;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.aehooo.tdsqn.annotations.TextureInfo;
import com.aehooo.tdsqn.resources.ImageAlligator3000;
import com.aehooo.tdsqn.resources.TextureName;
import com.aehooo.tdsqn.utils.Vector2D;

public class GameEntity {

	private IThisGameSprite sprite;
	private Vector2D pos;

	private boolean animated;

	public GameEntity(final float pX, final float pY) throws Exception {
		this(new Vector2D(pX, pY));
	}

	public GameEntity(final Vector2D pos) throws Exception {
		this.setPos(pos);

		this.initializeSprite(pos);
	}

	private void initializeSprite(final Vector2D pos) throws Exception {
		TextureInfo annotation = this.getClass().getAnnotation(
				TextureInfo.class);
		if (annotation == null) {
			this.setSprite(null);
		} else {
			TextureName textureName = annotation.name();

			TiledTextureRegion tiledTexture = ImageAlligator3000
					.getTiledTexture(textureName);

			TextureRegion texture = ImageAlligator3000.getTexture(textureName);

			if (tiledTexture != null) {
				this.setSprite(new ThisGameAnimatedSprite(pos.getX(), pos
						.getY(), tiledTexture));
				this.animated = true;
			} else if (texture != null) {
				this.setSprite(new ThisGameSprite(pos.getX(), pos.getY(),
						texture));
				this.animated = false;
			} else {
				throw new Exception("Sprite '" + textureName
						+ "' não encontrado");
			}

			this.setTouchHandler(new ITouchHandler() {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
						final float pTouchAreaLocalX,
						final float pTouchAreaLocalY) {
					return GameEntity.this.onAreaTouched(pSceneTouchEvent,
							pTouchAreaLocalX, pTouchAreaLocalY);
				}
			});
		}
	}

	public Vector2D getPos() {
		return this.pos;
	}

	public void setPos(final Vector2D v) {
		this.pos = v;
		if (this.sprite != null) {
			this.sprite.setPosition(v.getX(), v.getY());
		}
	}

	public void setPos(final float x, final float y) {
		this.setPos(new Vector2D(x, y));
	}

	public IThisGameSprite getSprite() {
		return this.sprite;
	}

	public void setSprite(final IThisGameSprite sprite) {
		this.sprite = sprite;
		this.sprite.setPosition(this.pos.getX(), this.pos.getY());
	}

	public void setTouchHandler(final ITouchHandler touchHandler) {
		if (this.sprite != null) {
			this.sprite.setTouchHandler(touchHandler);
		}
	}

	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		return false;
	}

	protected void animate(final long[] pFrameDurations, final int[] pFrames) {
		if (this.animated) {
			AnimatedSprite aSprite = (AnimatedSprite) this.sprite;
			aSprite.animate(pFrameDurations, pFrames);
		}
	}

}