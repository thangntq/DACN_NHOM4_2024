import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { form } from "src/app/shared/classes/product";
import { FormService } from "src/app/shared/services/form.service";

@Component({
  selector: "app-form",
  templateUrl: "./form.component.html",
  styleUrls: ["./form.component.scss"],
})
export class FormComponent implements OnInit {
  @Input() forms: any[] = [];
  public formList: form[] = [];

  @Output() formsFilter: EventEmitter<any> = new EventEmitter<any>();

  public collapse: boolean = false;

  constructor(public formService: FormService) {
    this.formService.getForm.subscribe((forms) => (this.formList = forms));
  }

  ngOnInit(): void {}

  get filterByForm() {
    const uniqueForms = [];
    this.formList.filter((form) => {
      uniqueForms.push(form.name);
    });
    return uniqueForms;
  }

  appliedFilter(event) {
    let index = this.forms.indexOf(event.target.value); // checked and unchecked value
    if (event.target.checked)
      this.forms.push(event.target.value); // push in array cheked value
    else this.forms.splice(index, 1); // removed in array unchecked value

    let forms = this.forms.length
      ? { form: this.forms.join(",") }
      : { form: null };
    this.formsFilter.emit(forms);
  }

  // check if the item are selected
  checked(item) {
    if (this.forms.indexOf(item) != -1) {
      return true;
    }
  }
}
