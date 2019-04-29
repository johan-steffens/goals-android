package za.co.steff.goals.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Strings;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.steff.goals.R;
import za.co.steff.goals.common.type.ChallengeType;
import za.co.steff.goals.common.type.PaletteType;
import za.co.steff.goals.common.util.DateUtil;
import za.co.steff.goals.data.database.entity.Aspect;
import za.co.steff.goals.data.database.entity.Challenge;

public class ChallengesListAdapter extends BaseAdapter {

    private Context context;
    private List<Aspect> aspects;

    private ChallengesListEventListener listener;

    public ChallengesListAdapter(Context context, List<Aspect> aspects) {
        this.context = context;
        this.aspects = aspects;
    }

    @Override
    public int getCount() {
        if(aspects != null)
            return aspects.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(aspects != null)
            return aspects.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Inflate view for the first time
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_challenges_list, null);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Aspect aspect = (Aspect) getItem(position);
        if(aspect != null) {
            // If a challenge is set
            if(! aspect.getChallenge().isNull()) {
                holder.layoutChallenge.setVisibility(View.VISIBLE);
                holder.layoutNoChallenge.setVisibility(View.GONE);

                Challenge challenge = aspect.getChallenge().getTarget();
                holder.txtAspect.setText(aspect.getName());
                holder.txtAspect.setTextColor(context.getResources().getColor(PaletteType.getDarkRes(aspect.getPalette().getTarget().getPaletteType())));
                holder.txtType.setText(ChallengeType.names().get(challenge.getType()));
                holder.txtCompletion.setText(challenge.getCompletionCondition());
                holder.txtSince.setText("since " + DateUtil.getNicePhraseFromDate(challenge.getCreatedAt(), false));
                holder.txtSince.setTextColor(context.getResources().getColor(PaletteType.getLightRes(aspect.getPalette().getTarget().getPaletteType())));
                holder.txtProgress.setTextColor(context.getResources().getColor(PaletteType.getLightRes(aspect.getPalette().getTarget().getPaletteType())));
                holder.viewProgressBackground.setBackgroundColor(context.getResources().getColor(PaletteType.getLightRes(aspect.getPalette().getTarget().getPaletteType())));
                holder.viewProgressForeground.setBackgroundColor(context.getResources().getColor(PaletteType.getDarkRes(aspect.getPalette().getTarget().getPaletteType())));

                // Set daily
                if(challenge.getType() == ChallengeType.Daily && DateUtil.isToday(challenge.getUpdatedAt())) {
                    holder.imgChecked.setVisibility(View.VISIBLE);
                    holder.imgUnchecked.setVisibility(View.GONE);
                } else if(challenge.getType() == ChallengeType.Daily && ! DateUtil.isToday(challenge.getUpdatedAt())) {
                    holder.imgChecked.setVisibility(View.GONE);
                    holder.imgUnchecked.setVisibility(View.VISIBLE);
                } else {
                    holder.imgChecked.setVisibility(View.GONE);
                    holder.imgUnchecked.setVisibility(View.GONE);
                }

                // Rather show expiry text if it's set
                if(challenge.getExpiresAt() != null) {
                    holder.txtSince.setText("expires " + DateUtil.getNicePhraseFromDate(challenge.getExpiresAt(), false));
                }

                DecimalFormat format = new DecimalFormat("#,###,###");
                if(challenge.getType() == ChallengeType.Percentage) {
                    holder.txtProgress.setText("" + challenge.getProgress() + "%");
                } else if(challenge.getType() == ChallengeType.Daily) {
                    holder.txtProgress.setText("" + format.format(challenge.getProgress()) + " / " + format.format(challenge.getGoal()) + " days");
                } else if(challenge.getType() == ChallengeType.Mission) {
                    holder.txtProgress.setText("" + (challenge.getGoal() == challenge.getProgress() ? "Complete" : "Incomplete"));
                } else {
                    holder.txtProgress.setText("" + format.format(challenge.getProgress()) + " / " + format.format(challenge.getGoal()));
                }

                // Set width of progress bar
                ViewGroup.LayoutParams layoutParams = holder.viewProgressForeground.getLayoutParams();
                layoutParams.width = Math.round((float) parent.getWidth() * ((float) challenge.getProgress() / (float) challenge.getGoal()));
                holder.viewProgressForeground.setLayoutParams(layoutParams);
            }
            // If a challenge is not set
            else {
                holder.layoutNoChallenge.setVisibility(View.VISIBLE);
                holder.layoutChallenge.setVisibility(View.GONE);
                holder.txtAspectName.setText(aspect.getName());
                holder.txtAspectName.setTextColor(context.getResources().getColor(PaletteType.getDarkRes(aspect.getPalette().getTarget().getPaletteType())));
                holder.txtAspectGoal.setText(aspect.getGoal());
                holder.txtAspectGoal.setTextColor(context.getResources().getColor(PaletteType.getLightRes(aspect.getPalette().getTarget().getPaletteType())));
                holder.txtClickToAdd.setTextColor(context.getResources().getColor(PaletteType.getLightRes(aspect.getPalette().getTarget().getPaletteType())));

                if(Strings.isNullOrEmpty(aspect.getGoal())) {
                    holder.txtAspectGoal.setVisibility(View.GONE);
                } else {
                    holder.txtAspectGoal.setVisibility(View.VISIBLE);
                }
            }

            // Set selection listener
            convertView.setOnClickListener((view) -> {
                listener.onChallengeItemSelected(aspect, position);
            });
        }

        // Set holder as tag of view and return
        convertView.setTag(holder);
        return convertView;
    }

    public void setListener(ChallengesListEventListener listener) {
        this.listener = listener;
    }

    class ViewHolder {

        @BindView(R.id.layoutChallenge)
        View layoutChallenge;
        @BindView(R.id.txtAspect)
        TextView txtAspect;
        @BindView(R.id.txtType)
        TextView txtType;
        @BindView(R.id.imgChecked)
        ImageView imgChecked;
        @BindView(R.id.imgUnchecked)
        ImageView imgUnchecked;
        @BindView(R.id.txtCompletion)
        TextView txtCompletion;
        @BindView(R.id.txtSince)
        TextView txtSince;
        @BindView(R.id.txtProgress)
        TextView txtProgress;
        @BindView(R.id.viewProgressBackground)
        View viewProgressBackground;
        @BindView(R.id.viewProgressForeground)
        View viewProgressForeground;

        @BindView(R.id.layoutNoChallenge)
        View layoutNoChallenge;
        @BindView(R.id.txtAspectName)
        TextView txtAspectName;
        @BindView(R.id.txtAspectGoal)
        TextView txtAspectGoal;
        @BindView(R.id.txtClickToAdd)
        TextView txtClickToAdd;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    public interface ChallengesListEventListener {

        void onChallengeItemSelected(Aspect aspect, int position);

    }
}
