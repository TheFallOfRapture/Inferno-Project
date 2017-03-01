package com.inferno.graphics.shaders;

import com.morph.engine.graphics.Shader;

public class TetrisShader extends Shader<TetrisShaderUniforms> {
	public TetrisShader() {
		super("shaders/tetrisSV");
	}

	protected void initUniforms(Shader<TetrisShaderUniforms> shader) {
		uniforms = new TetrisShaderUniforms();
		uniforms.init(shader);
	}
}
