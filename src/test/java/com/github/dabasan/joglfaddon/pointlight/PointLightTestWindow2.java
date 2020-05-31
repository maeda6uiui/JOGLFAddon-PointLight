package com.github.dabasan.joglfaddon.pointlight;

import static com.github.dabasan.basis.coloru8.ColorU8Functions.*;
import static com.github.dabasan.basis.vector.VectorFunctions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.dabasan.basis.vector.Vector;
import com.github.dabasan.joglf.gl.input.keyboard.KeyboardEnum;
import com.github.dabasan.joglf.gl.input.mouse.MouseEnum;
import com.github.dabasan.joglf.gl.model.Model3DFunctions;
import com.github.dabasan.joglf.gl.shader.ShaderProgram;
import com.github.dabasan.joglf.gl.util.camera.FreeCamera;
import com.github.dabasan.joglf.gl.window.JOGLFWindow;

class PointLightTestWindow2 extends JOGLFWindow {
	private List<Integer> point_light_handles;
	private int plane_handle;

	private FreeCamera camera;

	private Random random;

	@Override
	public void Init() {
		PointLightMgr.Initialize();
		point_light_handles = new ArrayList<>();

		plane_handle = Model3DFunctions.LoadModel("./Data/Model/OBJ/Plane/subdivided_plane.obj");
		Model3DFunctions.RemoveAllPrograms(plane_handle);
		Model3DFunctions.AddProgram(plane_handle, new ShaderProgram("dabasan/point_light/gouraud"));

		camera = new FreeCamera();
		camera.SetPosition(VGet(0.0f, 50.0f, 0.0f));

		random = new Random();
	}

	@Override
	public void Update() {
		int front = this.GetKeyboardPressingCount(KeyboardEnum.KEY_W);
		int back = this.GetKeyboardPressingCount(KeyboardEnum.KEY_S);
		int right = this.GetKeyboardPressingCount(KeyboardEnum.KEY_D);
		int left = this.GetKeyboardPressingCount(KeyboardEnum.KEY_A);

		int diff_x;
		int diff_y;
		if (this.GetMousePressingCount(MouseEnum.MOUSE_MIDDLE) > 0) {
			diff_x = this.GetCursorDiffX();
			diff_y = this.GetCursorDiffY();
		} else {
			diff_x = 0;
			diff_y = 0;
		}

		camera.Translate(front, back, right, left);
		camera.Rotate(diff_x, diff_y);
		camera.Update();

		if (this.GetKeyboardPressingCount(KeyboardEnum.KEY_ENTER) == 1) {
			int point_light_handle = PointLightMgr.CreatePointLight(ShadingMethod.GOURAUD);

			Vector position = camera.GetPosition();
			float r = random.nextFloat();
			float g = random.nextFloat();
			float b = random.nextFloat();
			PointLightMgr.SetPosition(point_light_handle, position);
			PointLightMgr.SetDiffuseColor(point_light_handle, GetColorU8(r, g, b, 1.0f));

			PointLightMgr.SetK(point_light_handle, 0.0f, 0.01f, 0.001f);

			point_light_handles.add(point_light_handle);
		}
		PointLightMgr.Update();
	}

	@Override
	public void Draw() {
		Model3DFunctions.DrawModel(plane_handle);
	}
}
