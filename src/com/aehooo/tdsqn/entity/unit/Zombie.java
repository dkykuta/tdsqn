package com.aehooo.tdsqn.entity.unit;

import com.aehooo.tdsqn.annotations.APS;
import com.aehooo.tdsqn.annotations.BuildAction;
import com.aehooo.tdsqn.annotations.TextureInfo;
import com.aehooo.tdsqn.annotations.Vel;
import com.aehooo.tdsqn.entity.action.Action;
import com.aehooo.tdsqn.enums.GameTargetType;
import com.aehooo.tdsqn.resources.TextureName;

@TextureInfo(name = TextureName.ZOMBIE, linhas = { "normal-baixo",
		"normal-direita", "normal-cima", "normal-esquerda" })
@APS(0.2)
@Vel(0.5)
public class Zombie extends BasicUnit {

	public Zombie() throws Exception {
		super();
	}

	@Override
	public String getName() {
		return "Zombie";
	}

	@BuildAction(targetType = GameTargetType.TOWER)
	public void buildAction(final Action action) {
		action.getSprite().setBlue(0);
		this.animateOnce(3);
		action.damage(30, 0);
	}
}
