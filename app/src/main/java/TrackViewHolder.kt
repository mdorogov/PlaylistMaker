import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val trackName: TextView
    val artistName: TextView
    val trackDuration: TextView
    val artwork: ImageView

    init {
        trackName = itemView.findViewById(R.id.track_title)
        artistName = itemView.findViewById(R.id.artist_name)
        trackDuration = itemView.findViewById(R.id.track_duration)
        artwork = itemView.findViewById(R.id.artwork)
    }

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackDuration.text = model.trackDuration
        setArtwork(model.artwork)
    }

    private fun setArtwork(art: String) {
        Glide.with(artwork)
            .load(art)
            .placeholder(R.drawable.ic_library)
            .transform(RoundedCorners(dpToPx(2f, artwork)))
            .into(artwork)
    }

    fun dpToPx(dp: Float, context: ImageView): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}