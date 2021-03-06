from pyxform import xls2json_backends
import xlwt
import re
import StringIO


def convert_csv_to_xls(csv_repr):
    """
    This method should be moved into pyxform
    """
    def _add_contents_to_sheet(sheet, contents):
        cols = []
        for row in contents:
            for key in row.keys():
                if key not in cols:
                    cols.append(key)
        for ci, col in enumerate(cols):
            sheet.write(0, ci, col)
        for ri, row in enumerate(contents):
            for ci, col in enumerate(cols):
                val = row.get(col, None)
                if val:
                    sheet.write(ri+1, ci, val)
    encoded_csv = csv_repr.decode("utf-8").encode("utf-8")
    dict_repr = xls2json_backends.csv_to_dict(StringIO.StringIO(encoded_csv))
    workbook = xlwt.Workbook()
    for sheet_name in dict_repr.keys():
        # pyxform.xls2json_backends adds "_header" items for each sheet.....
        if not re.match(r".*_header$", sheet_name):
            cur_sheet = workbook.add_sheet(sheet_name)
            _add_contents_to_sheet(cur_sheet, dict_repr[sheet_name])
    string_io = StringIO.StringIO()
    workbook.save(string_io)
    string_io.seek(0)
    return string_io