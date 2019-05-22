package org.dhis2.data.forms.dataentry.fields.orgUnit;

import androidx.fragment.app.FragmentManager;

import org.dhis2.data.forms.dataentry.fields.FormViewHolder;
import org.dhis2.data.forms.dataentry.fields.RowAction;
import org.dhis2.databinding.FormOrgUnitBinding;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.text.TextUtils.isEmpty;

/**
 * QUADRAM. Created by ppajuelo on 19/03/2018.
 */

public class OrgUnitHolder extends FormViewHolder {
    private final FormOrgUnitBinding binding;
    private final Observable<List<OrganisationUnitModel>> orgUnitsObservable;
    private List<OrganisationUnitModel> orgUnits;
    private CompositeDisposable compositeDisposable;
    private OrgUnitViewModel model;

    OrgUnitHolder(FragmentManager fm, FormOrgUnitBinding binding, FlowableProcessor<RowAction> processor, Observable<List<OrganisationUnitModel>> orgUnits, Observable<List<OrganisationUnitLevel>> levels) {
        super(binding);
        this.binding = binding;
        compositeDisposable = new CompositeDisposable();

        this.orgUnitsObservable = orgUnits;

        binding.orgUnitView.setListener(orgUnitUid -> {
            processor.onNext(RowAction.create(model.uid(), orgUnitUid));
        });

//        getOrgUnits();
    }

    @Override
    public void dispose() {
        compositeDisposable.clear();
    }

    public void update(OrgUnitViewModel viewModel) {
        this.model = viewModel;
        String uid_value_name = viewModel.value();
        String ouUid = null;
        String ouName = null;
        if (!isEmpty(uid_value_name)) {
            ouUid = uid_value_name.split("_ou_")[0];
            ouName = uid_value_name.split("_ou_")[1];
        }

        binding.orgUnitView.setObjectStyle(viewModel.objectStyle());
        binding.orgUnitView.setLabel(viewModel.label(), viewModel.mandatory());
        descriptionText = viewModel.description();
        binding.orgUnitView.setDescription(descriptionText);
        binding.orgUnitView.setWarning(viewModel.warning(), viewModel.error());
        binding.orgUnitView.setValue(ouUid, ouName);
        binding.orgUnitView.getEditText().setText(ouName);
        binding.orgUnitView.updateEditable(viewModel.editable());


    }

    private String getOrgUnitName(String value) {
        String orgUnitName = null;
        if (orgUnits != null) {
            for (OrganisationUnitModel orgUnit : orgUnits) {
                if (orgUnit.uid().equals(value))
                    orgUnitName = orgUnit.displayName();
            }
        }
        return orgUnitName;
    }

    private void getOrgUnits() {
        compositeDisposable.add(orgUnitsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        orgUnitViewModels ->
                        {
                            this.orgUnits = orgUnitViewModels;
                            if (model.value() != null) {
                                /*this.inputLayout.setHintAnimationEnabled(false);
                                this.editText.setText(getOrgUnitName(model.value()));
                                this.inputLayout.setHintAnimationEnabled(true);*/
                                update(model);
                            }
                        },
                        Timber::d
                )
        );
    }
}
