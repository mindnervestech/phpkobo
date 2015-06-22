define [
        'cs!xlform/model.aliases',
        'cs!xlform/model.survey',
        'cs!test/fixtures/surveys',
        'cs!xlform/model.configs'
        ], (
            $aliases,
            $survey,
            $surveys,
            $configs
            )->


  describe 'rank.tests', ->
    describe 'survey imports ranks >', ->
      beforeEach ->
        @survey = $survey.Survey.load {
            survey: [
              ["type", "name", "label", "kobo--rank-items"],
              {type: "begin rank", name: "koborank", label: "Label", "kobo--rank-items": "needs"},
              ["rank__level", "rnk1", "Rank Level 1"],
              ["rank__level", "rnk2", "Rank Level 2"],
              {type: "end rank"},
            ],
            'choices': [
              ['list name', 'name', 'label'],
              ['needs', 'food', 'Food'],
              ['needs', 'water', 'Water'],
              ['needs', 'shelter', 'Shelter'],
            ],
          }

      it 'can import a simple rank group', ->
        rankRow = @survey.rows.at(0)
        expect(rankRow.get('kobo--rank-items').get('value')).toBeDefined()
        expect(rankRow).toBeDefined()
        expect(rankRow._rankRows.length).toBe(2)
        expect(rankRow._rankLevels.options.length).toBe(3)

      describe 'clone a rank group', ->
        beforeEach ->
          @rankRow = @survey.rows.at(0)
          expect(@rankRow.get('kobo--rank-items').get('value')).toBeDefined()
          @clonedRow = @rankRow.clone()
          @survey._insertRowInPlace(@clonedRow, previous: @rankRow)

        it 'has the correct structure', ->
          expect(@survey.rows.length).toBe(2)
          expect(@survey.choices.length).toBe(2)
          _n = 0
          @survey.forEachRow (r) -> _n++
          expect(_n).toBe(6)

        it 'exports to csv', ->
          pkg = @survey.toCsvJson()
          [r1, r2, r3, r_end,
            cr1, cr2, cr3, cr_end] = pkg.survey.rowObjects
          expect(r1.label).toEqual(cr1.label)
          expect(r1['kobo--rank-items']).not.toEqual(cr1['kobo--rank-items'])
          expect(r_end.type).toEqual('end rank')
          expect(cr_end.type).toEqual('end rank')
          expect(_.pluck(pkg.choices.rowObjects, 'name')).toEqual([
              "food", "water", "shelter",
              "food", "water", "shelter",
            ])

        it 'exports to json', ->
          pkg = @survey.toJSON()
          [r1, r2, r3, r_end,
            cr1, cr2, cr3, cr_end] = pkg.survey
          expect(r1.label).toEqual(cr1.label)
          expect(r1['kobo--rank-items']).not.toEqual(cr1['kobo--rank-items'])
          expect(r_end.type).toEqual('end rank')
          expect(cr_end.type).toEqual('end rank')
          # each choice in pkg.choices comes in this format:
          #   list_name: [
          #     {name: 'name', label: 'label'}
          #   ]

          # test the flattened names look right
          expect(_.chain(pkg.choices)
                    .values()
                    .flatten()
                    .pluck('name')
                    .value()).toEqual([
                      "food", "water", "shelter",
                      "food", "water", "shelter",
                    ])

  describe 'score.tests', ->
    describe 'survey imports scores >', ->
      beforeEach ->
        @survey = $survey.Survey.load {
            survey: [
              ["type", "name", "label", "kobo--score-choices"],
              {type: "begin score", name: "koboskore", label: "Label", "kobo--score-choices": "koboskorechoices"},
              ["score__row", "skore_1", "Score 1"],
              ["score__row", "skore_2", "Score 2"],
              {type: "end score"},
            ],
            'choices': [
              ['list name', 'name', 'label'],
              ['koboskorechoices', 'ok', 'Okay'],
              ['koboskorechoices', 'not_ok', 'Not okay'],
            ],
          }

      it 'can import a simple score group', ->
        scoreRow = @survey.rows.at(0)
        expect(scoreRow.get('kobo--score-choices').get('value')).toBeDefined()
        expect(scoreRow).toBeDefined()

      it 'scores can be exported', ->
        output = @survey.toJSON()
        expect(output.survey.length).toBe(4)
        expect(output.survey[0].type).toBe('begin score')
        expect(output['choices']).toBeDefined()
        expect(output['choices']['koboskorechoices']).toEqual(
            [ {name: 'ok', label: 'Okay'}, {name: 'not_ok', label: 'Not okay'} ]
          )

  describe 'group.tests', ->
    _firstGroup = (s)->
      _.first s.rows.filter (r,i)-> r.constructor.name is "Group"
    _lastGroup = (s)->
      _.last s.rows.filter (r,i)-> r.constructor.name is "Group"

    describe 'survey imports groups >', ->
      beforeEach ->
        @survey = $survey.Survey.load({
            survey: [
                ['type',        'name', 'label'],
                ['begin group', 'grp1', 'Group1'],
                ['text',        'g1q1', 'Group1Question1'],
                ['end group'],
              ]
          })

      it 'can import a simple group', ->
        first_group = _firstGroup(@survey)
        expect(first_group).toBeDefined()
        expect(first_group.rows.length).toBe(1)
      it 'can add a group to the survey', ->
        @survey.addRow type: 'group', name: 'grp2'
        expect(@survey.rows.length).toBe(2)
        expect(_lastGroup(@survey).rows.length).toBe(0)
      it 'leaves empty group labels intact', ->
        survey = $survey.Survey.load({
            survey: [
                ['type',        'name', 'label'],
                ['begin group', 'grp1', null],
                ['text',        'g1q1', 'Group1Question1'],
                ['end group'],
              ]
          })

        first_group = _firstGroup survey

        expect(first_group.getValue 'label').toBe ''
      describe 'groups can be exported >', ->
        it 'works with a simple group', ->
          expect(@survey.toCSV().split('\n').length).toBe(8)
        it 'works with a nested group', ->
          survey = $survey.Survey.load({
              survey: [
                ['type',        'name', 'label'],
                ['begin group', 'grp1', 'Group1'],
                ['text',        'g1q1', 'Grp2Question1'],
                ['begin group', 'g1g2', 'Group1Question1'],
                ['text',        'g1g2q1', 'Grp2Question1'],
                ['end group'],
                ['end group'],
              ]
            })
          expect(survey.toCSV().split('\n').length).toBe(11)

    it 'can import repeats', ->
      survey = $survey.Survey.load({
          survey: [
            ['type',        'name', 'label'],
            ['begin repeat', 'grp1', 'Group1'],
            ['text',        'g1q1', 'Group1Question1'],
            ['end repeat'],
          ]
        })
      first_row = survey.rows.first()
      expect(first_row).toBeDefined()
      expect(first_row.constructor.name).toBe("Group")
      expect(first_row._isRepeat()).toBeTruthy()
    describe 'fails on unmatched group types', ->
      expectFailure = (msg, surv)->
        execFn = ->
          survey = $survey.Survey.load(surv)
        expect(execFn).toThrow()

      it 'fails with unclosed group', ->
        expectFailure 'unclosed', """
        survey,,,
        ,type,name,label
        ,begin group,grp1,Group1
        """
        expectFailure 'unclosed', {
            survey: [
              ['type',        'name', 'label'          ],
              #-----------------------------------------
              ['begin group', 'grp1', 'Group1'         ],
              ['text',        'g1q1', 'Group1Question1'],
            ]
          }
        expectFailure 'unclosed', {
            survey: [
              ['type',        'name', 'label'],
              ['begin group', 'grp1', 'Group1'],
            ]
          }
      it 'fails with mismatched group and repeat', ->
        expectFailure 'mismatch', """
        survey,,,
        ,type,name,label
        ,begin group,grp1,Group1
        ,end repeat,,
        """
        expectFailure 'mismatch', {
            survey: [
              ['type'       , 'name', 'label' ],
              #--------------------------------
              ['begin group', 'grp1', 'Group1'],
              ['end repeat'                   ],
            ]
          }
      it 'fails with mismatched group and repeat', ->
        expectFailure 'mismatch', """
        survey,,,
        ,type,name,label
        ,begin repeat,grp1,Group1
        ,end group,,
        """

    describe 'group creation', ->
      beforeEach ->
        @survey = $survey.Survey.load({
            survey: [
              ['type', 'name', 'label'],
              #------------------------
              ['text', 'q1',   'Q1'   ],
              ['text', 'q2',   'Q2'   ],
              ['text', 'q3',   'Q3'   ],
              ['text', 'q4',   'Q4'   ],
              ['text', 'q5',   'Q5'   ],
            ]
          })

      describe 'group naming', ->
        beforeEach ->
          @getNames = (s)->
            _n = 'noname'
            names = []
            s.forEachRow (
                    (r)->
                      name = r.get('name')?.get('value') or _n
                      names.push name
                  ), includeGroups: true
            names

          expect(@survey._allRows().length).toBe(5)
          @rows = for n in [0,2,4]
            @survey.rows.at(n)

        it 'uses opts.label when not undefined', ->
          @survey._addGroup(label: 'My Group', __rows: @rows)
          expect(_firstGroup(@survey).getValue('label')).toBe 'My Group'
        it 'uses default label when no label is passed', ->
          @survey._addGroup(__rows: @rows)
          expect(_firstGroup(@survey).getValue('label')).toBe $configs.newGroupDetails.label.value

      describe 'can create group with existing rows', ->
        beforeEach ->
          @getNames = (s)->
            _n = 'noname'
            names = []
            s.forEachRow (
                    (r)->
                      name = r.get('name')?.get('value') or _n
                      names.push name
                  ), includeGroups: true
            names

          expect(@survey._allRows().length).toBe(5)
          rows = for n in [0,2,4]
            @survey.rows.at(n)

          @survey._addGroup(label: 'My Group', __rows: rows)
        it 'and has the right number of rows', ->
          expect(@survey._allRows().length).toBe(5)
        it 'has the right order of names', ->
          @survey.finalize()
          names = @getNames(@survey)
          names[0] = 'group_xxxxxxx'
          expect(names).toEqual(["group_xxxxxxx", "q1", "q3", "q5", "q2", "q4"])

        describe 'can generate missing names on finalize', ->
          beforeEach ->
            @grp = _firstGroup(@survey)

          it 'and has a finalize method', ->
            expect(@grp.finalize).toBeDefined()
          it 'and has finalize called on survey finalize', ->
            spyOn @grp, 'finalize'
            @survey.finalize()
            expect(@grp.finalize).toHaveBeenCalled()
          it 'has the correct name', ->
            @survey.finalize()
            names = @getNames(@survey)
            names[0] = 'group_xxxxxxx'
            expect(names).toEqual(['group_xxxxxxx', 'q1', 'q3', 'q5', 'q2', 'q4'])

    describe 'group manipulation', ->
      beforeEach ->
        @survey = $survey.Survey.load({
            survey: [
              ['type'       , 'name', 'label'           ],
              #------------------------------------------
              ['text'       , 'q1'  , 'Q1'              ],
              ['begin group', 'grp1', 'Group1'          ],
              ['text'       , 'g1q1', 'Group1Question1' ],
              ['end group'                              ],
              ['text'       , 'q2'  , 'Q2'              ],
            ]
          })
        @g1 = _firstGroup @survey

        @getNames = (s)->
          _n = 'noname'
          names = []
          s.forEachRow (
                  (r)->
                    name = r.get('name')?.get('value') or _n
                    names.push name
                ), includeGroups: true
          names
      it 'group can be deleted', ->
        g1 = _firstGroup @survey
        expect(@survey._allRows().length).toBe(3)
        @survey.remove g1
        expect(@survey._allRows().length).toBe(2)
      it 'group can be detached from parent', ->
        expect(@getNames(@survey)).toEqual(['q1', 'grp1', 'g1q1', 'q2'])
        @g1.detach()
        expect(@getNames(@survey)).toEqual(['q1', 'q2'])
      it 'group can be split apart', ->
        expect(@getNames(@survey)).toEqual(['q1', 'grp1', 'g1q1', 'q2'])
        @g1.splitApart()
        expect(@getNames(@survey)).toEqual(['q1', 'g1q1', 'q2'])
      describe 'nested group can be split apart', ->
        beforeEach ->
          @survey = $survey.Survey.load({
              survey: [
                ['type',        'name', 'label' ],
                #--------------------------------
                ['begin group', 'grp1', 'Group1'],
                ['begin group', 'grp2', 'Group1'],
                ['text'       , 'q1'  , 'Q1'    ],
                ['end group'                    ],
                ['end group'                    ],
                ['text'       , 'q2'  , 'Q2'    ],
              ]
            })
          @g1 = _firstGroup @survey
          @g2 = @g1.rows.at(0)
        it 'is set up', ->
          expect(@g2.constructor.key).toEqual("group")
          expect(@getNames(@survey)).toEqual(['grp1', 'grp2', 'q1', 'q2'])

        it 'can break apart outer group', ->
          @g1.splitApart()
          expect(@getNames(@survey)).toEqual(['grp2', 'q1', 'q2'])

        it 'can break apart outer group', ->
          @g2.splitApart()
          expect(@getNames(@survey)).toEqual(['grp1', 'q1', 'q2'])
