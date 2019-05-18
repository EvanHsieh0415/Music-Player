package info.u_team.music_player.render;

import java.util.function.Function;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.to_u_team_core.export.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.math.MathHelper;

public class RenderOverlayMusicDisplay {
	
	private final ITrackManager manager;
	
	private int width;
	private int height;
	
	private final RenderScrollingText title;
	private final RenderScrollingText author;
	
	private final RenderScalingText position;
	private final RenderScalingText duration;
	
	public RenderOverlayMusicDisplay() {
		manager = MusicPlayerManager.getPlayer().getTrackManager();
		
		height = 40;
		width = 120;
		
		final FontRenderer fontRender = Minecraft.getInstance().fontRenderer;
		
		title = new RenderScrollingText(() -> fontRender, () -> getValueOfTrack(track -> track.getInfo().getFixedTitle()));
		title.setStepSize(0.5F);
		title.setColor(0xFFFF00);
		title.setWidth(109);
		title.setSpeedTime(35);
		
		author = new RenderScrollingText(() -> fontRender, () -> getValueOfTrack(track -> track.getInfo().getFixedAuthor()));
		author.setStepSize(0.5F);
		author.setColor(0xFFFF00);
		author.setScale(0.75F);
		author.setWidth(109);
		author.setSpeedTime(35);
		
		position = new RenderScalingText(() -> fontRender, () -> getValueOfTrack(GuiTrackUtils::getFormattedPosition));
		position.setColor(0xFFFF00);
		position.setScale(0.5F);
		
		duration = new RenderScalingText(() -> fontRender, () -> getValueOfTrack(GuiTrackUtils::getFormattedDuration));
		duration.setColor(0xFFFF00);
		duration.setScale(0.5F);
		
	}
	
	private String getValueOfTrack(Function<IAudioTrack, String> function) {
		final IAudioTrack track = manager.getCurrentTrack();
		if (track != null) {
			return function.apply(track);
		}
		return null;
	}
	
	public void draw(float x, float y) {
		final IAudioTrack track = manager.getCurrentTrack();
		if (track == null) {
			return;
		}
		final int intX = MathHelper.ceil(x);
		final int intY = MathHelper.ceil(y);
		
		// Background
		Gui.drawRect(intX, intY, width, height, 0xFF212121);
		
		// Progressbar
		final double progress = (double) track.getPosition() / track.getDuration();
		
		final int progressBarWidth = intX + width - 12;
		
		Gui.drawRect(intX + 6, intY + 23, progressBarWidth, intY + 26, 0xFF555555);
		Gui.drawRect(intX + 6, intY + 23, (int) (intX + 6 + (progress * progressBarWidth)), intY + 26, 0xFF3e9100);
		
		// Draw strings
		title.draw(x + 3, y + 3);
		author.draw(x + 3, y + 13);
		
		position.draw(x + 6, y + 28);
		duration.draw(x + width - 12 - duration.getTextWidth(), y + 28);
	}
	
}